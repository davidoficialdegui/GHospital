plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    java
    jacoco
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
    implementation("com.github.librepdf:openpdf:1.3.30")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

// ── Tests unitarios (excluye integración y rendimiento) ──────────
tasks.test {
    useJUnitPlatform {
        excludeTags("integration", "performance")
    }
    finalizedBy(tasks.jacocoTestReport)
}

// ── Tests de integración ─────────────────────────────────────────
tasks.register<Test>("integrationTest") {
    description = "Ejecuta los tests de integración"
    group = "verification"
    useJUnitPlatform {
        includeTags("integration")
    }
    shouldRunAfter(tasks.test)
}

// ── Tests de rendimiento ─────────────────────────────────────────
tasks.register<Test>("performanceTest") {
    description = "Ejecuta los tests de rendimiento"
    group = "verification"
    useJUnitPlatform {
        includeTags("performance")
    }
    shouldRunAfter(tasks.test)
}

// ── Jacoco ───────────────────────────────────────────────────────
jacoco {
    toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
}

// ── Javadoc ──────────────────────────────────────────────────────
tasks.javadoc {
    title = "GHospital - Documentación API"
    options {
        (this as StandardJavadocDocletOptions).apply {
            encoding = "UTF-8"
            charSet = "UTF-8"
            author(true)
            version(true)
            use(true)
            windowTitle = "GHospital API"
            locale = "es_ES"
            addStringOption("Xdoclint:none", "-quiet")
        }
    }
}
