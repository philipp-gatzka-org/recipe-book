import org.flywaydb.gradle.task.FlywayMigrateTask

plugins {
    alias(libs.plugins.flyway)
}

val migrationUrl: String by project
val migrationUser: String by project
val migrationPassword: String by project
val migrationSchema: String by project

flyway {
    url = migrationUrl
    user = migrationUser
    password = migrationPassword
    schemas = arrayOf(migrationSchema)
    locations = arrayOf("filesystem:src/main/resources/db/postgres")
}

tasks {
    register<FlywayMigrateTask>("flywayMigrateH2") {
        url = "jdbc:h2:mem:recipe-book;MODE=PostgreSQL;DB_CLOSE_ON_EXIT=FALSE"
        user = migrationUser
        password = migrationPassword
        schemas = arrayOf(migrationSchema)
        locations = arrayOf("filesystem:src/main/resources/db/h2")
    }
}

buildscript {
    dependencies {
        classpath(platform(libs.spring.boot.dependencies))
        classpath(libs.database.postgres)
        classpath(libs.database.h2)
        classpath(libs.flyway.postgres)
    }
}