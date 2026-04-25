plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    java
}

group = "com.gestionHospitalaria"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("com.github.librepdf:openpdf:1.3.30")   // ← así, con paréntesis y comillas dobles
}

// ↓ AÑADE ESTE BLOQUE COMPLETO
tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks.test {
    useJUnitPlatform()
}