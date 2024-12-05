package ch.gatzka.rest.service;

import ch.gatzka.GenerativeDataTest;
import ch.gatzka.rest.request.account.RegisterRequest;
import ch.gatzka.service.repository.AccountRepository;
import ch.gatzka.tables.records.AccountRecord;
import com.icegreen.greenmail.util.GreenMailUtil;
import javax.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static ch.gatzka.constants.Routes.ROUTE_ACCOUNT_REGISTER;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AccountServiceTest extends GenerativeDataTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void givenRegisterRequest_whenRegister_thenAccountShouldBeRegistered() {
        RegisterRequest registerRequest = new RegisterRequest(string(10), string(10), email(), string(10), "en");
        performPostOk(ROUTE_ACCOUNT_REGISTER, registerRequest);
        assertTrue(accountRepository.existsByEmail(registerRequest.email()));
        MimeMessage nextEmail = getNextEmail();
        String body = GreenMailUtil.getBody(nextEmail);
        AccountRecord account = accountRepository.getByEmail(registerRequest.email());
        assertTrue(body.contains(account.getEmailVerificationCode()));
    }

}