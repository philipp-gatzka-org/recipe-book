package ch.gatzka.rest.request.account;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record RegisterRequest(
        @NotNull @NotEmpty @Length(max = 50) String firstname,
        @NotNull @NotEmpty @Length(max = 50) String lastname,
        @NotNull @NotEmpty @Email String email,
        @NotNull @NotEmpty String password,
        @NotNull @NotEmpty String languageCode
) {
}
