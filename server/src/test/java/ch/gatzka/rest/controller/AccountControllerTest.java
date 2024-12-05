package ch.gatzka.rest.controller;

import ch.gatzka.GenerativeDataTest;
import ch.gatzka.rest.request.account.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static ch.gatzka.constants.Messages.EMAIL_EXISTS;
import static ch.gatzka.constants.Messages.LANGUAGE_CODE_DOES_NOT_EXIST;

@SpringBootTest
public class AccountControllerTest extends GenerativeDataTest {

    @Test
    void givenRegisteredEmail_whenRegister_thenReturnEMAIL_IS_EXISTS() {
        RegisteredAccount registeredAccount = registeredAccount();
        RegisterRequest registerRequest = new RegisterRequest(string(10), string(10), registeredAccount.email(), string(10), "en");
        performPostBadRequest("/account/register", registerRequest, EMAIL_EXISTS);
    }

    @Test
    void givenRandomLanguageCode_whenRegister_thenReturnLANGUAGE_CODE_DOES_NOT_EXIST() {
        RegisterRequest registerRequest = new RegisterRequest(string(10), string(10), email(), string(10), string(2));
        performPostBadRequest("/account/register", registerRequest, LANGUAGE_CODE_DOES_NOT_EXIST);
    }

}
