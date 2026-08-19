plugins {
    id("java")
    alias(libs.plugins.spring.dependency.management)
}

group = "tech.skullprogrammer.bguard"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.web)
    implementation(libs.spring.boot.jpa)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    //TEST
    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.boot.jpa.test)
    testRuntimeOnly("com.h2database:h2:2.4.240")
    testRuntimeOnly(libs.postgres.driver)
    testImplementation(platform(libs.spring.boot.bom))
    testImplementation(platform(libs.testcontainer.bom))
    testImplementation("org.testcontainers:testcontainers")
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.flyway.core)
    testImplementation(libs.flyway.postgres)
    testImplementation(libs.flyway.spring)
}

tasks.test {
    useJUnitPlatform()
}