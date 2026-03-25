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
    // 🔥 SPRING WEB (para @RestController, @PostMapping, etc.)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // 🔥 SPRING JPA (para @Entity, @Column, etc.)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // Base de datos (ejemplo con H2 para pruebas)
    runtimeOnly("com.h2database:h2")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    
    implementation("org.springframework.boot:spring-boot-starter-security")
}

tasks.test {
    useJUnitPlatform()
}