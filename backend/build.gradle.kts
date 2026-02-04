import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.lang.System.getenv

group = "fr.gouv.dgampa"
// x-release-please-start-version
version = "2.60.4"
// x-release-please-end
description = "RapportNav"

val kotlinVersion by extra("2.3.0")
val serializationVersion by extra("1.6.2")
val springVersion by extra("4.0.1")
val testcontainersVersion by extra("1.19.3")
val flywayVersion by extra("11.20.0")

plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm") version "2.3.0"
  kotlin("plugin.spring") version "2.3.0"
  kotlin("plugin.jpa") version "2.3.0"
  id("org.springframework.boot") version "4.0.1"
  id("io.spring.dependency-management") version "1.1.7"
  id("org.owasp.dependencycheck") version "12.1.0"
  id("org.flywaydb.flyway") version "11.20.0"
//  id("io.sentry.jvm.gradle") version "5.12.2"
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
    // force any dependency like following :
     dependency("org.apache.commons:commons-lang3:3.19.0")
  }
}

dependencies {
  developmentOnly("org.springframework.boot:spring-boot-devtools")
  implementation("org.postgresql:postgresql:42.7.8")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-data-rest")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-graphql")
  implementation("org.springframework.boot:spring-boot-starter-log4j2")
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.boot:spring-boot-starter-security-oauth2-resource-server")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("org.springframework.boot:spring-boot-starter-flyway")
  implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib")
  implementation("org.flywaydb:flyway-database-postgresql:$flywayVersion")
  implementation("tools.jackson.module:jackson-module-kotlin")
  implementation("io.jsonwebtoken:jjwt-api:0.12.7")
  implementation("jakarta.xml.bind:jakarta.xml.bind-api")
  implementation("org.locationtech.jts:jts-core:1.20.0")
  implementation("org.locationtech.jts.io:jts-io-common:1.20.0")
  // sentry
  implementation(platform("io.sentry:sentry-bom:8.29.0"))
  implementation("io.sentry:sentry-spring-boot-4")
  implementation("io.sentry:sentry-log4j2")
  // apache poi for xml and docs
  // the two following apache poi dependencies should have the same version
  implementation("org.apache.poi:poi:5.4.1")
  implementation("org.apache.poi:poi-ooxml:5.4.1")
  implementation("org.apache.commons:commons-text:1.14.0")
  implementation("org.jodconverter:jodconverter-local-lo:4.4.11")
  implementation("com.neovisionaries:nv-i18n:1.29")
  implementation("org.wiremock:wiremock-standalone:4.0.0-beta.24")
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
  testImplementation("org.springframework.boot:spring-boot-starter-jackson-test")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("org.testcontainers:testcontainers")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:postgresql")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
}

buildscript {
  dependencies {
    classpath("org.flywaydb:flyway-database-postgresql:11.20.0")
  }
}


java {
  sourceCompatibility = JavaVersion.VERSION_21
}

kotlin {
  jvmToolchain(21)
}

tasks.withType<KotlinCompile>().configureEach {
  compilerOptions {
    freeCompilerArgs.add("-Xjsr305=strict")
    jvmTarget.set(JvmTarget.JVM_21)
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
