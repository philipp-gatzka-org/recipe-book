package ch.gatzka.rest.controller;

import ch.gatzka.constants.Routes;
import ch.gatzka.rest.Validator;
import ch.gatzka.rest.endpoint.AccountEndpoint;
import ch.gatzka.rest.request.account.RegisterRequest;
import ch.gatzka.rest.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ch.gatzka.constants.Routes.ROUTE_ACCOUNT;

@RestController
@RequestMapping(ROUTE_ACCOUNT)
@RequiredArgsConstructor
public class AccountController implements AccountEndpoint {

    private final Validator validator;
    private final AccountService accountService;

    @Override
    public void register(RegisterRequest request) {
        validator.validateThat(validator.emailDoesNotExist(request.email()), validator.languageCodeExists(request.languageCode()));
        accountService.register(request);
    }

}
