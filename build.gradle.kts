import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
  kotlin("jvm") version "2.0.20"
  id("org.jetbrains.dokka") version "1.9.20"
  `maven-publish`
  signing
  id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

data class SemVer(
  val major: Int,
  val minor: Int,
  val patch: Int,
) {
  inline val featureVersion get() = "$major.$minor.0"

  inline val gitTag get() = "v$major.$minor.$patch"

  inline val sedExp get() = "SemVer(major = $major, minor = $minor, patch = $patch)"

  override fun toString() = "$major.$minor.$patch"
}

val projectVersion = SemVer(major = 0, minor = 1, patch = 0)

group = "io.foxcapades.kt"
version = projectVersion.toString()

repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    languageVersion = JavaLanguageVersion.of(21)
    vendor = JvmVendorSpec.AMAZON
  }

  java {
    withSourcesJar()
    withJavadocJar()
  }
}

dependencies {
  testImplementation("org.mockito:mockito-junit-jupiter:5.13.0")
  testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
  testImplementation("org.mockito:mockito-core:5.13.0")
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()

  testLogging {
    events(
      TestLogEvent.PASSED,
      TestLogEvent.FAILED,
      TestLogEvent.SKIPPED,
    )

    showStandardStreams = true
  }
}

tasks.dokkaHtml {
  val targetDir = file("docs/dokka/${projectVersion.featureVersion}")

  outputDirectory = targetDir

  doFirst {
    targetDir.deleteRecursively()
  }
}

nexusPublishing {
  repositories {
    sonatype {
      nexusUrl = uri("https://s01.oss.sonatype.org/service/local/")
      snapshotRepositoryUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
      username = project.findProperty("nexus.user") as String? ?: System.getenv("NEXUS_USER")
      password = project.findProperty("nexus.pass") as String? ?: System.getenv("NEXUS_PASS")
    }
  }
}

publishing {
  publications {
    create<MavenPublication>("gpr") {
      from(components["java"])
      pom {
        name.set("Kotlin CLI Call Builder")
        description.set("Provides delegates, annotations, and tools to generate CLI calls from " +
          "classes, making it easier to define type-safe, validating wrappers around " +
          "external CLI tools.")
        url.set("https://github.com/foxcapades/lib-kt-cli-builder")

        licenses {
          license {
            name.set("MIT")
          }
        }

        developers {
          developer {
            id.set("epharper")
            name.set("Elizabeth Paige Harper")
            email.set("foxcapades.io@gmail.com")
            url.set("https://github.com/foxcapades")
          }
        }

        scm {
          connection.set("scm:git:git://github.com/foxcapades/lib-kt-cli-builder.git")
          developerConnection.set("scm:git:ssh://github.com/foxcapades/lib-kt-cli-builder.git")
          url.set("https://github.com/foxcapades/lib-kt-cli-builder")
        }
      }
    }
  }
}

signing {
  useGpgCmd()

  sign(configurations.archives.get())
  sign(publishing.publications["gpr"])
}

tasks.create("bump-feature") {
  group = "Custom"

  doLast {
    val newVersion = projectVersion.copy(minor = projectVersion.minor + 1, patch = 0)

    with(ProcessBuilder("sed", "s/${projectVersion.sedExp}/${newVersion.sedExp}/", "build.gradle.kts")) {
      val tmp = file("build.gradle.kts.tmp")
      redirectOutput(tmp)

      if (start().waitFor() != 0) {
        throw GradleException("failed to patch gradle file!")
      }

      val cur = file("build.gradle.kts")
      cur.delete()
      tmp.renameTo(cur)
    }
  }
}

tasks.create("release") {
  group = "Custom"

  doFirst {
    with(ProcessBuilder("git", "diff-index", "--quiet", "HEAD")) {
      if (start().waitFor() != 0) {
        throw GradleException("git workspace is not clean!")
      }
    }
  }

  dependsOn(
    "publishToSonatype",
    "closeAndReleaseSonatypeStagingRepository"
  )
}

tasks.create("update-readme") {
  group = "Custom"

  doLast {
    val tmp    = file("readme.adoc.tmp")
    val readme = file("readme.adoc")

    tmp.delete()
    tmp.bufferedWriter().use { w ->
      readme.bufferedReader().use { r ->
        r.lineSequence()
          .map {
            when {
              !it.startsWith(':')                -> it
              it.startsWith(":version-actual:")  -> ":version-actual: $version"
              it.startsWith(":version-feature:") -> ":version-feature: ${projectVersion.featureVersion}"
              else                               -> it
            }
          }
          .forEach {
            w.write(it)
            w.newLine()
          }
      }
    }

    readme.delete()
    tmp.renameTo(readme)
  }
}
