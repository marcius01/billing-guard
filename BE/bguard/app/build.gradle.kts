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
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.web)
    developmentOnly(libs.spring.boot.devtools)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // bguard modules
    implementation(project(":api"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
