package ch.gatzka.rest.endpoint;

import ch.gatzka.rest.request.account.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AccountEndpoint {

    @PostMapping(name = "Register account", value = "register", consumes = MediaType.APPLICATION_JSON_VALUE)
    void register(@Valid @RequestBody RegisterRequest request);

}
