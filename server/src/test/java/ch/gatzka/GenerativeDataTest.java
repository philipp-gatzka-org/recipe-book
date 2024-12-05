package ch.gatzka;

import ch.gatzka.rest.request.account.RegisterRequest;
import ch.gatzka.service.repository.AccountRepository;
import ch.gatzka.service.repository.LanguageRepository;
import ch.gatzka.tables.records.AccountRecord;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetup;
import java.util.UUID;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import static ch.gatzka.constants.Routes.ROUTE_ACCOUNT_REGISTER;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public abstract class GenerativeDataTest extends RestTest {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Autowired
    private AccountRepository accountRepository;

    protected final GreenMail greenMail = new GreenMail(ServerSetup.SMTP);

    public String string(int length) {
        StringBuilder string = new StringBuilder(UUID.randomUUID().toString());

        while (string.length() < length) {
            string.append(UUID.randomUUID());
        }

        return string.substring(0, length);
    }

    public String email() {
        return "%s@%s.%s".formatted(string(10), string(4), string(3));
    }

    public RegisteredAccount registeredAccount() {
        RegisterRequest registerRequest = new RegisterRequest(string(15), string(15), email(), string(15), "en");
        performPost(ROUTE_ACCOUNT_REGISTER, registerRequest, HttpStatus.OK);
        MimeMessage nextEmail = getNextEmail();
        String body = GreenMailUtil.getBody(nextEmail);
        AccountRecord account = accountRepository.getByEmail(registerRequest.email());
        assertTrue(body.contains(account.getEmailVerificationCode()));
        return new RegisteredAccount(registerRequest.email(), registerRequest.password(), account.getEmailVerificationCode());
    }

    @BeforeEach
    public void startGreenMail() {
        greenMail.start();
        greenMail.setUser(mailUsername, mailPassword);
    }

    @AfterEach
    public void stopGreenMail() {
        greenMail.stop();
    }

    public MimeMessage getNextEmail() {
        greenMail.waitForIncomingEmail(1);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        return receivedMessages[receivedMessages.length - 1];
    }

    public record RegisteredAccount(String email, String password, String verificationCode) {

    }

    @Autowired
    private LanguageRepository languageRepository;

    @BeforeEach
    void initializeDatabase() {
        if (!languageRepository.existsByCode("en")) {
            languageRepository.insert(entity -> entity.setCode("en").setName("English"));
        }
    }

}
