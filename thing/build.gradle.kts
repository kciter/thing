description = "A rule-based entity management library written in Kotlin"

plugins {
  `maven-publish`
}

apply(plugin = "kotlin")
apply(plugin = "maven-publish")

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  testImplementation(kotlin("test"))
}

publishing {
  publications {
    create<MavenPublication>("thing") {
      groupId = "${project.group}"
      artifactId = "thing"
      version = "0.0.1"
      pom {
        description.set(project.description)
      }
    }
  }
}
