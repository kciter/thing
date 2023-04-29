val artifactId = "thing-spring"
val springBootVersion = "2.7.0"

description = "Thing for Spring Boot"

plugins {
  kotlin("jvm") version "1.7.20"
  kotlin("kapt") version "1.7.20"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  `maven-publish`
}

dependencies {
  kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${springBootVersion}")
  kapt("org.springframework.boot:spring-boot-configuration-processor:${springBootVersion}")

  implementation(project(":thing"))
  implementation("org.springframework.boot:spring-boot-autoconfigure:${springBootVersion}")
  implementation("org.springframework.boot:spring-boot-starter:${springBootVersion}")
  implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
}

publishing {
  publications {
    create<MavenPublication>(artifactId) {
      groupId = "${project.group}"
      artifactId = artifactId
      version = "${project.version}"
      pom {
        description.set(project.description)
      }
    }
  }
}
