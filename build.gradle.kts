plugins {
  kotlin("jvm") version "1.7.20"
}

allprojects {
  repositories {
    mavenLocal()
    mavenCentral()
  }
}

subprojects {
  apply(plugin = "kotlin")

  group = "so.kciter"
  version = "1.0.0"

  java.sourceCompatibility = JavaVersion.VERSION_11

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
  }
}
