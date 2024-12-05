import org.jooq.meta.jaxb.MatcherTransformType
import org.testcontainers.containers.PostgreSQLContainer

plugins {
    id("java-library")
    alias(libs.plugins.jooq)
    alias(libs.plugins.flyway)
}

buildscript {
    dependencies {
        classpath(platform(libs.spring.boot.dependencies))
        classpath(libs.testcontainers.postgresql)
        classpath(libs.database.postgres)
        classpath(libs.flyway.postgres)
    }
}

val postgresContainer =
    PostgreSQLContainer<Nothing>("postgres:latest").apply {
        withDatabaseName("recipe_book")
        withUsername("postgres")
        withPassword("postgres")
        start()
    }

dependencies {
    jooqCodegen(platform(libs.spring.boot.dependencies))
    jooqCodegen(libs.database.postgres)

    implementation(libs.jooq)
}

flyway {
    url = postgresContainer.jdbcUrl
    user = postgresContainer.username
    password = postgresContainer.password
    schemas = arrayOf("recipe_book")
}

jooq {
    configuration {
        generator {
            database {
                jdbc {
                    url = postgresContainer.jdbcUrl
                    driver = "org.postgresql.Driver"
                    user = postgresContainer.username
                    password = postgresContainer.password
                    inputSchema = "recipe_book"
                    isIncludeSequences = true
                    isIncludeSystemSequences = true
                    excludes = "flyway_schema_history"
                }
            }
            strategy {
                matchers {
                    tables {
                        table {
                            tableClass {
                                transform = MatcherTransformType.PASCAL
                                expression = "$0_Table"
                            }
                        }
                    }
                }
            }
            target {
                packageName = project.group.toString()
            }
            generate {
                isFluentSetters = true
                isSequences = true
            }
        }
    }
}

tasks {
    jooqCodegen {
        dependsOn(flywayMigrate)
    }
    compileJava {
        dependsOn(jooqCodegen)
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    sourceSets["main"].java {
        srcDir("build/generated-sources/jooq")
    }
}