package ch.gatzka;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@Slf4j
@TestConfiguration(proxyBeanMethods = false)
public class PostgresSQLContainerConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");
        postgres.start();
        Flyway.configure().dataSource(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword()).schemas("recipe_book").load().migrate();
        return postgres;
    }

}
