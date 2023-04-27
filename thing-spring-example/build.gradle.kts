val springBootVersion = "2.7.0"

description = "thing-spring example"

plugins {
  kotlin("plugin.spring") version "1.7.0"
  id("org.springframework.boot") version "2.7.0"
}

dependencies {
  implementation(project(":thing"))
  implementation(project(":thing-spring"))
  implementation("org.springframework.boot:spring-boot-starter:${springBootVersion}")
  implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
}
