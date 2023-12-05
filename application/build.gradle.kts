plugins {
  id("java")
}

group = "br.com.fullcycle"
version = "0.0.1-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":domain"))
  testImplementation(platform("org.junit:junit-bom:5.9.1"))
  testImplementation("org.junit.jupiter:junit-jupiter")
  testImplementation("org.assertj:assertj-core:3.24.2")
}

tasks.test {
  useJUnitPlatform()
}
