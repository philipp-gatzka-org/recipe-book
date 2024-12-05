package ch.gatzka.rest.endpoint;

import ch.gatzka.rest.request.account.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static ch.gatzka.constants.Routes.ROUTE_REGISTER;

public interface AccountEndpoint {

    @PostMapping(name = "Register account", value = ROUTE_REGISTER, consumes = MediaType.APPLICATION_JSON_VALUE)
    void register(@Valid @RequestBody RegisterRequest request);

}
