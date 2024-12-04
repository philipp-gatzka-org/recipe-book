plugins {
    id("java")
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
    testRuntimeOnly(libs.database.h2)

    // implementation(libs.spring.boot.actuator)
    // testImplementation(libs.spring.boot.test)
    // testImplementation(libs.spring.boot.testcontainers)
    // testImplementation(libs.testcontainers.junit)
    // testImplementation(libs.testcontainers.postgresql)
    // testRuntimeOnly(libs.junit.platform)
}
