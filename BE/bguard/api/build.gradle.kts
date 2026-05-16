plugins {
    id("java")
    alias(libs.plugins.spring.dependency.management)
}

group = "tech.skullprogrammer.bguard"
version = "unspecified"

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
    implementation (libs.postgres.driver)
    implementation(libs.mapstruct)
    annotationProcessor(libs.mapstruct.processor)
    implementation(libs.bean.validation)
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.spring.boot.webmvc.test)
    // bguard modules
    implementation(project(":domain"))
}

tasks.test {
    useJUnitPlatform()
}