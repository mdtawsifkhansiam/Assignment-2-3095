plugins {
    java
    id("org.springframework.boot") version "3.2.2" // FIX: Added missing quote here
    id("io.spring.dependency-management") version "1.1.4"
}

group = "ca.gbc.comp3095"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.data:spring-data-redis")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Observability
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // Resilience
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.1.0")

    // Kafka & Schema Registry
    implementation("org.springframework.kafka:spring-kafka")
    implementation("io.confluent:kafka-json-schema-serializer:7.5.0")

    // Docs
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    testImplementation("org.testcontainers:kafka")
}

tasks.withType<Test> {
    useJUnitPlatform()
}