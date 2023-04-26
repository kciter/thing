import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

description = "A rule-based entity management library written in kotlin"

plugins {
  kotlin("jvm")
  `maven-publish`
}

publishing {
  publications.withType<MavenPublication> {
    pom {
      description.set(project.description)
    }
  }
}
dependencies {
  implementation(kotlin("stdlib-jdk8"))
}
repositories {
  mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
  jvmTarget = "1.8"
}
