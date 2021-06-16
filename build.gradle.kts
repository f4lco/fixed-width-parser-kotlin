plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("maven-publish")
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

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.github.f4lco"
            artifactId = "fixed-width-parser-kotlin"
            version = "0.1.0-SNAPSHOT"
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
