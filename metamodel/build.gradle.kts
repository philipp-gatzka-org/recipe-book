import org.jooq.meta.jaxb.MatcherTransformType

plugins {
    id("java-library")
    alias(libs.plugins.jooq)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    sourceSets["main"].java {
        srcDir("build/generated-sources/jooq")
    }
}

dependencies {
    implementation(libs.jooq)
    implementation(libs.jetbrains.annotations)
    jooqCodegen(libs.jooq.meta)
}

jooq {
    configuration {
        generator {
            database {
                name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                properties {
                    property {
                        key = "scripts"
                        value = "src/main/resources"
                    }
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
            }
        }
    }
}

tasks {
    withType<JavaCompile> {
        dependsOn(jooqCodegen)
    }
    register("createSchemaFile") {
        val tablesDir = file("src/main/resources")
        val outputFile = file("${layout.buildDirectory.asFile.get()}/schema.sql")

        doLast {
            outputFile.delete()
            outputFile.parentFile.mkdirs()
            tablesDir.listFiles()?.sorted()?.forEach { tableFile ->
                if (tableFile.isFile) {
                    outputFile.appendText("-- ${tableFile.name}\n")
                    outputFile.appendText("${tableFile.readText()}\n\n")
                }
            }
        }
    }
}
