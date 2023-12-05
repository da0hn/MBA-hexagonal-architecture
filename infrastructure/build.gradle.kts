plugins {
  id("java")
  id("org.springframework.boot") version "3.1.2"
  id("io.spring.dependency-management") version "1.1.2"
}

group = "br.com.fullcycle"
version = "0.0.1-SNAPSHOT"

java {
  sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":domain"))
  implementation(project(":application"))

  implementation("io.hypersistence:hypersistence-tsid:2.1.0")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-graphql")
  implementation("org.springframework.boot:spring-boot-starter-web")

  runtimeOnly("com.mysql:mysql-connector-j")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework:spring-webflux")
  testImplementation("org.springframework.graphql:spring-graphql-test")

  testRuntimeOnly("com.h2database:h2")

  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
  useJUnitPlatform()
}
