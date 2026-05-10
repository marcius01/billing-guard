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
    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.spring.boot.test)
    testImplementation(libs.spring.boot.jpa.test)
    testRuntimeOnly("com.h2database:h2:2.4.240")
}

tasks.test {
    useJUnitPlatform()
}