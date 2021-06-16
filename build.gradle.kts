plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
}

val fixedWidthParserVersion: String by project
val junitVersion: String by project

val providedLibs: Configuration by configurations.creating

configurations {
    compileOnly.get().extendsFrom(providedLibs)
    testImplementation.get().extendsFrom(providedLibs)
}

dependencies {
    providedLibs("com.github.joutvhu:fixed-width-parser:$fixedWidthParserVersion")
    providedLibs(kotlin("reflect"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
