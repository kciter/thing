val artifactId = "thing"

description = "A rule-based entity management library written in Kotlin"

plugins {
  `maven-publish`
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  testImplementation(kotlin("test"))
}

publishing {
  publications {
    create<MavenPublication>(artifactId) {
      groupId = "${project.group}"
      artifactId = artifactId
      version = "0.0.2"
      pom {
        description.set(project.description)
      }
    }
  }
}
