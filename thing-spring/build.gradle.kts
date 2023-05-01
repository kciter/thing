val artifactId = "thing-spring"

description = "Thing for Spring Boot"

plugins {
  id("io.spring.dependency-management") version "1.0.15.RELEASE"
  id("org.springframework.boot") version "2.7.6" apply false
  kotlin("jvm") version "1.7.20"
  kotlin("kapt") version "1.7.20"
  `maven-publish`
}

ext["kotlin.version"] = "1.7.20"

dependencies {
  compileOnly(project(":thing"))
  compileOnly("org.springframework.boot:spring-boot-starter-web")
}

dependencyManagement {
  imports {
    mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
  }
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
