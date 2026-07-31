plugins {
    java
//    id("org.springframework.boot")
//    id("io.spring.dependency-management")
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spring.boot)
}

group = "tech.skullprogrammer.bguard"
version = "0.0.1-SNAPSHOT"
description = "app"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.jpa)
    implementation(libs.open.api)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgres)
    implementation(libs.flyway.spring)
    developmentOnly(libs.spring.boot.devtools)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
//    implementation("org.postgresql:postgresql")
    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.21")
    // bguard modules
    implementation(project(":api"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
