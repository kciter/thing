plugins {
    kotlin("jvm") version "1.7.20"
}

allprojects {
  repositories {
    mavenCentral()
  }
}

subprojects {
  group = "so.kciter"
  version = "1.0.0"


}

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
