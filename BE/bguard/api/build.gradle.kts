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
    implementation(libs.open.csv)
    implementation(libs.spring.security)
    implementation("io.jsonwebtoken:jjwt-api:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.13.0")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.13.0")
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(libs.spring.boot.webmvc.test)
    testRuntimeOnly("com.h2database:h2:2.4.240")
    // bguard modules
    implementation(project(":domain"))
}

tasks.test {
    useJUnitPlatform()
}