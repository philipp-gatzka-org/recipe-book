package ch.gatzka.rest;

import ch.gatzka.service.repository.AccountRepository;
import ch.gatzka.service.repository.LanguageRepository;
import java.util.concurrent.Callable;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.yaml.snakeyaml.util.Tuple;

@Service
@RequiredArgsConstructor
public final class Validator {

    private final AccountRepository accountRepository;

    private final LanguageRepository languageRepository;

    @SafeVarargs
    @SneakyThrows
    public final void validateThat(Tuple<String, Callable<Boolean>>... validations) {
        for (Tuple<String, Callable<Boolean>> validation : validations) {
            if (!validation._2().call()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, validation._1());
            }
        }
    }

    public Tuple<String, Callable<Boolean>> emailDoesNotExist(String email) {
        return new Tuple<>("EMAIL_EXISTS", () -> !accountRepository.existsByEmail(email));
    }

    public Tuple<String, Callable<Boolean>> emailExists(String email) {
        return new Tuple<>("EMAIL_DOES_NOT_EXIST", () -> accountRepository.existsByEmail(email));
    }

    public Tuple<String, Callable<Boolean>> languageCodeExists(String languageCode) {
        return new Tuple<>("LANGUAGE_CODE_DOES_NOT_EXIST", () -> languageRepository.existsByCode(languageCode));
    }

}
