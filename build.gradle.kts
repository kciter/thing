val ossrhUsername: String? by project
val ossrhPassword: String? by project

val signingKeyId: String? by project
val signingKey: String? by project
val signingPassword: String? by project

plugins {
  kotlin("jvm") version "1.7.20"
  kotlin("kapt") version "1.7.20"
  `maven-publish`
  signing
}

allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "kotlin")
  apply(plugin = "kotlin-kapt")
  apply(plugin = "maven-publish")
  apply(plugin = "signing")

  group = "so.kciter"
  version = "0.0.4"

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

    artifacts {
      archives(jar)
    }
  }

  configure<PublishingExtension> {
    repositories {
      maven {
        if (project.version.toString().endsWith("SNAPSHOT")) {
          setUrl("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        } else {
          setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        }

        credentials {
          username = ossrhUsername
          password = ossrhPassword
        }
      }
    }

    publications.withType<MavenPublication> {
      from(components["java"])

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
          contributor {
            name.set("Sally Oh")
            email.set("min27604@gmail.com")
            url.set("https://github.com/sallyjellyy")
          }
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
    }
  }

  signing {
    useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
    sign(publishing.publications)
  }
}
