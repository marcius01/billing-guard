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
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testAnnotationProcessor(libs.lombok)
    testCompileOnly(libs.lombok)
    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}