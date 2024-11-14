import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.System.getenv

group = "fr.gouv.dgampa"
version = "2.3.2"
description = "RapportNav"

val kotlinVersion by extra("1.9.24")
val serializationVersion by extra("1.6.2")
val springVersion by extra("3.3.5")
val testcontainersVersion by extra("1.19.3")
val flywayVersion by extra("10.10.0")

plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm") version "1.9.24"
  kotlin("plugin.spring") version "1.9.24"
  kotlin("plugin.jpa") version "1.9.24"
  id("org.springframework.boot") version "3.3.5"
  id("io.spring.dependency-management") version "1.1.4"
  id("org.owasp.dependencycheck") version "8.4.0"
  id("org.flywaydb.flyway") version "10.10.0"
  jacoco
}

springBoot {
  mainClass.set("fr.gouv.dgampa.rapportnav.RapportNavApplicationKt")

  buildInfo {
    properties {
      additional = mapOf(
        "commit.hash" to "COMMIT_TO_CHANGE",
      )
    }
  }
}

repositories {
  mavenCentral()
}

configurations.all {
  exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
}

dependencyManagement {
  imports {
    mavenBom("org.testcontainers:testcontainers-bom:$testcontainersVersion")
  }
  dependencies {
    dependency("org.springframework:spring-webmvc:6.1.14")
  }
}

dependencies {
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("org.postgresql:postgresql:42.7.3")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-data-rest:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-web:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-graphql:$springVersion")
  implementation("org.springframework.boot:spring-boot-autoconfigure:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-log4j2:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-security:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springVersion")
  implementation("org.springframework.boot:spring-boot-starter-cache:$springVersion")
  implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.flywaydb:flyway-core:$flywayVersion")
  implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
  implementation("org.n52.jackson:jackson-datatype-jts:1.2.10") {
    exclude(group = "org.locationtech.jts", module = "jts-core")
  }
  implementation("io.jsonwebtoken:jjwt-api:0.12.3")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
  implementation("org.springframework.security:spring-security-oauth2-jose:6.3.2")
  implementation("org.locationtech.jts:jts-core:1.19.0")
  implementation("io.swagger.core.v3:swagger-core:2.2.20")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
  implementation("io.sentry:sentry-spring-boot-starter-jakarta:7.1.0")
  implementation("io.sentry:sentry-log4j2:7.0.0")
  // the two following apoche poi dependencies should have the same version
  implementation("org.apache.poi:poi:5.3.0")
  implementation("org.apache.poi:poi-ooxml:5.3.0")
  implementation("org.apache.commons:commons-text:1.12.0")
  implementation("org.jodconverter:jodconverter-local-lo:4.4.7")
  implementation("com.neovisionaries:nv-i18n:1.29")
  testImplementation("org.springframework.boot:spring-boot-starter-test:$springVersion")
  testImplementation("org.springframework:spring-webflux:6.1.13")
  testImplementation("org.springframework.graphql:spring-graphql-test:1.2.4")
  testImplementation("org.testcontainers:testcontainers")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.springframework.security:spring-security-test:6.3.2")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

buildscript {
  dependencies {
    classpath("org.flywaydb:flyway-database-postgresql:10.10.0")
  }
}


java {
  sourceCompatibility = JavaVersion.VERSION_17
}

kotlin {
  jvmToolchain(17)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    jvmTarget = "17"
  }
}

tasks.named<Test>("test") {
  useJUnitPlatform()

  if (getenv("CI") != null) {
    testLogging {
      // set options for log level LIFECYCLE
      events("failed")
    }
  } else {
    testLogging {
      events("passed")
    }
  }
}

// for hot-reload:
tasks.register<Copy>("getDependencies") {
  from(sourceSets.main.get().runtimeClasspath)
  into("runtime/")

  doFirst {
    val runtimeDir = File("runtime")
    runtimeDir.deleteRecursively()
    runtimeDir.mkdir()
  }

  doLast {
    File("runtime").deleteRecursively()
  }
}

jacoco {
  toolVersion = "0.8.12"
  reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
  reports {
    xml.required.set(true)   // Enable XML report (for CI integration)
    html.required.set(true)  // Enable HTML report
    html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
  }
}

tasks.test {
  useJUnitPlatform()   // If you are using JUnit 5
  finalizedBy(tasks.jacocoTestReport)  // Generate the report after tests
}
