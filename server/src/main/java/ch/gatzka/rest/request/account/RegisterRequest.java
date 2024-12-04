package ch.gatzka.rest.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegisterRequest(
        @NotNull @NotEmpty @Length(max = 50) String firstname,
        @NotNull @NotEmpty @Length(max = 50) String lastname,
        @NotNull @NotEmpty @Email String email,
        @NotNull @NotEmpty String password,
        @NotNull @NotEmpty String languageCode
) {
}
