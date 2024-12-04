package ch.gatzka.rest.service;

import ch.gatzka.rest.endpoint.AccountEndpoint;
import ch.gatzka.rest.request.account.RegisterRequest;
import ch.gatzka.service.EmailService;
import ch.gatzka.service.repository.AccountRepository;
import ch.gatzka.service.repository.LanguageRepository;
import ch.gatzka.tables.records.LanguageRecord;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService implements AccountEndpoint {

    private final LanguageRepository languageRepository;

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    @Override
    public void register(RegisterRequest request) {
        LanguageRecord language = languageRepository.getByCode(request.languageCode());
        String emailVerificationCode = UUID.randomUUID().toString();

        accountRepository.insert(entity -> entity
                .setFirstname(request.firstname())
                .setLastname(request.lastname())
                .setEmail(request.email())
                .setPassword(passwordEncoder.encode(request.password()))
                .setLanguageId(language.getId())
                .setEmailVerificationCode(emailVerificationCode));

        emailService.sendEmail(request.email(), "Email Verification", EmailService.EMAIL_EMAIL_VERIFICATION, language.getCode(), request.lastname(), request.firstname(), emailVerificationCode);
    }


}
