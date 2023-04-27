val ossrhUsername: String? by project
val ossrhPassword: String? by project

val signingKeyId: String? by project
val signingKey: String? by project
val signingPassword: String? by project

plugins {
  kotlin("jvm") version "1.7.20"
  `maven-publish`
  signing
}

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://plugins.gradle.org/m2/")
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "maven-publish")

  group = "so.kciter"

  tasks {
    compileKotlin {
      kotlinOptions {
        jvmTarget = "11"
      }
    }

    compileTestKotlin {
      kotlinOptions {
        jvmTarget = "11"
      }
    }

    val sourcesJar by creating(Jar::class) {
      archiveClassifier.set("sources")
      from(sourceSets.main.get().allSource)
    }

    val javadocJar by creating(Jar::class) {
      dependsOn.add(javadoc)
      archiveClassifier.set("javadoc")
      from(javadoc)
    }

    artifacts {
      archives(sourcesJar)
      archives(javadocJar)
      archives(jar)
    }
  }

  publishing {
    repositories {
      maven {
        if (project.version.toString().endsWith("SNAPSHOT")) {
          setUrl("https://oss.sonatype.org/content/repositories/snapshots")
        } else {
          setUrl("https://oss.sonatype.org/service/local/staging/deploy/maven2")
        }

        credentials {
          username = ossrhUsername
          password = ossrhPassword
        }
      }
    }

    publications.withType<MavenPublication> {
      from(components["java"])

      artifact(tasks["sourcesJar"])
      artifact(tasks["javadocJar"])

      pom {
        packaging = "jar"
        name.set(project.name)
        url.set("https://github.com/kciter/thing")
        inceptionYear.set("2023")

        licenses {
          license {
            name.set("MIT License")
            url.set("https://opensource.org/licenses/MIT")
          }
        }

        developers {
          developer {
            id.set("kciter")
            name.set("Lee Sun-Hyoup")
            email.set("kciter@naver.com")
            url.set("https://github.com/kciter")
            timezone.set("Asia/Seoul")
          }
        }

        contributors {
        }

        scm {
          connection.set("scm:git:https://github.com/kciter/thing")
          developerConnection.set("scm:git:git@github.com:kciter/thing.git")
          url.set("https://github.com/kciter/thing")
        }

        issueManagement {
          system.set("GitHub")
          url.set("https://github.com/kciter/thing/issues")
        }

        ciManagement {
          system.set("GitHub")
          url.set("https://github.com/kciter/thing/actions?query=workflow%3Aci")
        }
      }

      configure<SigningExtension> {
        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
        sign(publications)
      }
    }
  }
}
