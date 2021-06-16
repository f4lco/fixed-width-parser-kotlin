dependencyResolutionManagement {

    val kotlinVersion: String by settings
    val ktlintVersion: String by settings

    pluginManagement {
        plugins {
            kotlin("jvm").version(kotlinVersion)
            id("org.jlleitschuh.gradle.ktlint").version(ktlintVersion)
        }
    }

    repositories {
        mavenCentral()
    }
}
