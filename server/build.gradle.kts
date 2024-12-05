plugins {
    id("java")
    id("jacoco")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.lombok)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(platform(libs.spring.boot.dependencies))
    implementation(libs.spring.boot.jooq)
    implementation(libs.spring.boot.mail)
    implementation(libs.spring.boot.security)
    implementation(libs.spring.boot.validation)
    implementation(libs.spring.boot.web)

    implementation(project(":metamodel"))

    implementation(libs.jetbrains.annotations)

    developmentOnly(platform(libs.spring.boot.dependencies))
    developmentOnly(libs.spring.boot.devtools)

    runtimeOnly(libs.database.postgres)

    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.flyway)
    testImplementation(libs.flyway.postgres)
    testImplementation(libs.greenmail)

    // implementation(libs.spring.boot.actuator)
    // testRuntimeOnly(libs.junit.platform)
}

tasks {
    jacocoTestReport {
        dependsOn(test)
    }
    test {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
        jvmArgs("-XX:+EnableDynamicAgentLoading", "-Xshare:off")
    }
}