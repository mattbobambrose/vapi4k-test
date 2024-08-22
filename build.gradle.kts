val kotlin_version: String by project
val logback_version: String by project
val vapi4k_version: String by project

plugins {
  val kotlinVersion: String by System.getProperties()
  val ktorVersion: String by System.getProperties()
  val versionsVersion: String by System.getProperties()

  java
  kotlin("jvm") version kotlinVersion
  id("io.ktor.plugin") version ktorVersion
  id("com.github.ben-manes.versions") version versionsVersion
}

group = "com.myapp"
version = "1.0.0"

application {
  mainClass.set("com.myapp.ApplicationKt")
}

ktor {
  fatJar {
    // Change this to whatever name you want
    archiveFileName.set("myapp.jar")
  }
}

repositories {
  google()
  mavenCentral()
  // Required for the vapi4k jars
  maven(url = "https://jitpack.io")
}

dependencies {
  implementation("com.github.mattbobambrose.vapi4k:vapi4k-core:$vapi4k_version")
  implementation("com.github.mattbobambrose.vapi4k:vapi4k-dbms:$vapi4k_version")

  testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

// Required for heroku deployments
tasks.register("stage") {
  dependsOn("build", "clean")
  doLast {
    println("Stage task completed")
  }
}

// Required for heroku deployments
tasks.named("build") {
  mustRunAfter("clean")
}