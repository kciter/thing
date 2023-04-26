description = "A rule-based entity management library written in kotlin"

plugins {
  kotlin("jvm")
  `maven-publish`
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.20")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  testImplementation(kotlin("test"))
}

//publishing {
//  publications.withType<MavenPublication> {
//    pom {
//      description.set(project.description)
//    }
//  }
//}
