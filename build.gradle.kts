plugins {
    alias(libs.plugins.lombok) apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.freefair.lombok")

    group = "ch.gatzka"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    plugins.withType<JavaPlugin> {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }
    }

}