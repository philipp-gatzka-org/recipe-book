import org.flywaydb.gradle.task.FlywayMigrateTask
import org.jooq.meta.jaxb.MatcherTransformType

plugins {
    id("java-library")
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq)
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    jooqCodegen(platform(libs.spring.boot.dependencies))
    implementation(libs.jooq)
    jooqCodegen(libs.database.h2)
}

val h2Connection = "jdbc:h2:file:~/recipe-book;MODE=PostgreSQL"
val migrationUrl = findProperty("migrationUrl") as? String ?: h2Connection
val migrationUser = findProperty("migrationUser") as? String ?: "sa"
val migrationPassword = findProperty("migrationPassword") as? String ?: ""
val migrationSchema = findProperty("migrationSchema") as? String ?: "recipe-book"

flyway {
    url = migrationUrl
    user = migrationUser
    password = migrationPassword
    schemas = arrayOf(migrationSchema)
    locations = arrayOf("filesystem:src/main/resources/db/postgres")
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    sourceSets["main"].java {
        srcDir("build/generated-sources/jooq")
    }
}

jooq {
    configuration {
        jdbc {
            driver = "org.h2.Driver"
            url = h2Connection
            user = migrationUser
            password = migrationPassword
        }
        generator {
            database {
                name = "org.jooq.meta.h2.H2Database"
                inputSchema = migrationSchema
                isIncludeSequences = true
                isIncludeSystemSequences = true
                excludes = """
                    FLYWAY_SCHEMA_HISTORY
                """.trimIndent()
            }
            generate {
                isFluentSetters = true
                isSequences = true
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
            }
            target {
                packageName = project.group.toString()
            }
        }
    }
}

tasks {
    register<FlywayMigrateTask>("flywayMigrateH2") {
        doFirst {
            val userHome = System.getProperty("user.home")
            File(userHome, "recipe-book.mv.db").delete()
        }

        url = migrationUrl
        user = migrationUser
        password = migrationPassword
        schemas = arrayOf(migrationSchema)
        locations = arrayOf("filesystem:src/main/resources/db/h2")
    }
    jooqCodegen {
        dependsOn(named("flywayMigrateH2"))
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