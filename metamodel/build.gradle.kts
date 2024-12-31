import org.flywaydb.gradle.task.FlywayMigrateTask
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    alias(libs.plugins.flyway)
}

buildscript {
    dependencies {
        classpath(platform(libs.spring.boot.dependencies))
        classpath(libs.postgresql.testcontainer)
        classpath(libs.postgresql.flyway)
        classpath(libs.postgresql.database)
    }
}

val postgresSQLContainer = PostgreSQLContainer<Nothing>("postgres:latest").apply {
    withDatabaseName("recipe_book")
    withUsername("postgres")
    withPassword("postgres")
    start()
}

val databaseCIUrl: String? = findProperty("database_ci_url") as String?
val databaseCIUser: String? = findProperty("database_ci_user") as String?
val databaseCIPassword: String? = findProperty("database_ci_password") as String?
val databaseCISchema: String? = findProperty("database_ci_schema") as String?

tasks {
    register<FlywayMigrateTask>("migrateDEV") {
        url = postgresSQLContainer.jdbcUrl
        user = postgresSQLContainer.username
        password = postgresSQLContainer.password
        schemas = arrayOf("recipe_book")
    }
    register<FlywayMigrateTask>("migrateCI") {
        doFirst {
            checkNotNull(databaseCIUrl) { "database_ci_url is not set" }
            checkNotNull(databaseCIUser) { "database_ci_user is not set" }
            checkNotNull(databaseCIPassword) { "database_ci_password is not set" }
            checkNotNull(databaseCISchema) { "database_ci_schema is not set" }
        }

        url = databaseCIUrl
        user = databaseCIUser
        password = databaseCIPassword
        schemas = arrayOf(databaseCISchema)
    }
    withType<JavaCompile> {
        dependsOn("migrateDEV")
    }
}
