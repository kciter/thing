val artifactId = "thing-spring"
val springBootVersion = "2.7.0"

description = "Thing for Spring Boot"

//plugins {
//  id("io.spring.dependency-management") version "1.0.11.RELEASE"
//  `maven-publish`
//  kotlin("jvm") version "1.7.10"
//  kotlin("kapt") version "1.7.10"
//}

apply {
  plugin("kotlin")
  plugin("kotlin-kapt")
}

dependencies {
//  kapt("org.springframework.boot:spring-boot-autoconfigure-processor:${springBootVersion}")
//  kapt("org.springframework.boot:spring-boot-configuration-processor:${springBootVersion}")

  api("so.kciter:thing")
  implementation("org.springframework.boot:spring-boot-starter:${springBootVersion}")
  implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")

  testImplementation(kotlin("test"))
}

publishing {
  publications {
    create<MavenPublication>(artifactId) {
      groupId = "${project.group}"
      artifactId = artifactId
      version = "0.0.1"
      pom {
        description.set(project.description)
      }
    }
  }
}
