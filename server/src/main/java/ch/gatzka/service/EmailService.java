package ch.gatzka.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import java.io.BufferedReader;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    public static final String EMAIL_EMAIL_VERIFICATION = "email-verification.html";

    @Value("${spring.mail.username}")
    private String mailUsername;

    private final JavaMailSender mailSender;

    @SneakyThrows
    private String readTemplate(String templateName, String languageCode) {
        ClassPathResource resource = new ClassPathResource("email" + File.separator + languageCode + File.separator + templateName);

        try (BufferedReader reader = Files.newBufferedReader(resource.getFile().toPath())) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        }
    }

    private String getEmail(String templateName, String languageCode, String... properties) {
        String email = readTemplate(templateName, languageCode);
        for (int i = 0; i < properties.length; i++) {
            email = email.replace("$" + i, properties[i]);
        }
        return email;
    }

    @SneakyThrows
    public void sendEmail(String to, String subject, String templateName, String languageCode, String... properties) {
        String email = getEmail(templateName, languageCode, properties);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        mimeMessage.setSubject(subject);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(mailUsername);
        helper.setTo(to);
        helper.setText(email, true);

        mailSender.send(mimeMessage);
    }

}
