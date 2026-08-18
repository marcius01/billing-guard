import org.gradle.api.tasks.SourceSetContainer
import org.gradle.kotlin.dsl.getByType
import org.springframework.boot.gradle.tasks.run.BootRun

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

evaluationDependsOn(":api")
evaluationDependsOn(":domain")

val apiSourceSets = project(":api")
    .extensions
    .getByType<SourceSetContainer>()

val domainSourceSets = project(":domain")
    .extensions
    .getByType<SourceSetContainer>()

tasks.named<BootRun>("bootRun") {
    dependsOn(
        ":api:classes",
        ":domain:classes"
    )

    val runtimeClasspathWithoutInternalJars =
        sourceSets.main.get().runtimeClasspath.filter { file ->
            val path = file.invariantSeparatorsPath

            !path.endsWith("/api/build/libs/api.jar") &&
                    !path.contains("/domain/build/libs/domain-")
        }

    classpath = files(
        sourceSets.main.get().output,
        apiSourceSets.main.get().output,
        domainSourceSets.main.get().output,
        runtimeClasspathWithoutInternalJars
    )
}
