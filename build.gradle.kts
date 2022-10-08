import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.3"
    id("io.spring.dependency-management") version "1.0.13.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.4")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.4")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.11")
    implementation("org.springdoc:springdoc-openapi-ui:1.6.11")
    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.11")

    implementation("io.github.microutils:kotlin-logging-jvm:3.0.0")

    runtimeOnly("com.h2database:h2:2.1.214")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.4")

    // Remember to add this for cucumber
    // https://thecodinganalyst.github.io/knowledgebase/No-tests-found-when-running-cucumber-junit/
    testImplementation("org.junit.vintage:junit-vintage-engine:5.9.0")

    testImplementation("io.mockk:mockk:1.13.2")
    testImplementation("io.cucumber:cucumber-java:7.8.1")
    testImplementation("io.cucumber:cucumber-spring:7.8.1")
    testImplementation("io.cucumber:cucumber-junit:7.8.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
