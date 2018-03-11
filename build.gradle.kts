/* Imports *******************************************************************/
import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.junit.platform.gradle.plugin.EnginesExtension
import org.junit.platform.gradle.plugin.FiltersExtension
import org.junit.platform.gradle.plugin.JUnitPlatformExtension
/* ***************************************************************************/

/* Properties ****************************************************************/
val htmlUnitDriverVersion by project
val jupiterVersion by project
val jvmTargetVersion by project
val kotlinStdLib by project
val projectDescription by project
val projectVersion by project
val seleniumVersion by project
val slf4jVersion by project
val spekVersion by project
/* ***************************************************************************/

/* Build script's setup ******************************************************/
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
    classpath("com.kncept.junit5.reporter:junit-reporter:1.1.0")
    classpath("org.junit.platform:junit-platform-gradle-plugin:1.1.0")
  }
}
/* ***************************************************************************/

/* Plugins *******************************************************************/
plugins {
  kotlin("jvm") version "1.2.30"
}

apply {
  plugin("com.jfrog.bintray")
  plugin("com.kncept.junit5.reporter")
  plugin("maven-publish")
  plugin("org.junit.platform.gradle.plugin")
}
/* ***************************************************************************/

/* Dependencies **************************************************************/
dependencies {
  compile(kotlin("$kotlinStdLib"))
  compile(kotlin("reflect"))
  compile("org.slf4j:slf4j-api:$slf4jVersion")
  compile("org.seleniumhq.selenium:selenium-api:$seleniumVersion")
  compile("org.seleniumhq.selenium:selenium-firefox-driver:$seleniumVersion")
  compile("org.seleniumhq.selenium:selenium-support:$seleniumVersion")

  testCompile("org.jetbrains.spek:spek-api:$spekVersion") {
    exclude(group = "org.jetbrains.kotlin")
  }
  testCompile("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
  testCompile("org.seleniumhq.selenium:htmlunit-driver:$htmlUnitDriverVersion")

  testRuntime("org.jetbrains.spek:spek-junit-platform-engine:$spekVersion") {
    exclude(group = "org.jetbrains.kotlin")
  }
  testRuntime("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

repositories {
  jcenter()
}
/* ***************************************************************************/

/* JUnit setup ***************************************************************/
configure<JUnitPlatformExtension> {
  filters {
    engines {
      include("jupiter", "spek")
    }
  }
}

fun JUnitPlatformExtension.filters(setup: FiltersExtension.() -> Unit) {
  when (this) {
    is ExtensionAware -> extensions.getByType(FiltersExtension::class.java).setup()
    else -> throw Exception("${this::class} must be an instance of ExtensionAware")
  }
}

fun FiltersExtension.engines(setup: EnginesExtension.() -> Unit) {
  when (this) {
    is ExtensionAware -> extensions.getByType(EnginesExtension::class.java).setup()
    else -> throw Exception("${this::class} must be an instance of ExtensionAware")
  }
}
/* ***************************************************************************/

/* Publication setup *********************************************************/
val sourcesJarTask = task<Jar>("sourceJar") {
  from(java.sourceSets["main"].allSource)

  classifier = "sources"
}

configure<PublishingExtension> {
  publications {
    create<MavenPublication>("JCenterPublication") {
      from(components["java"])
      artifact(sourcesJarTask)

      groupId = "com.github.epadronu"
      artifactId = "balin"
      version = "$projectVersion"
    }
  }
}

configure<BintrayExtension> {
  user = System.getenv("JFROG_USER")
  key = System.getenv("JFROG_KEY")

  setPublications("JCenterPublication")

  pkg.apply {
    repo = "maven"
    name = "balin"
    desc = "$projectDescription"
    vcsUrl = "https://github.com/EPadronU/balin.git"
    publish = true
    setLabels("kotlin", "selenium", "web", "geb", "automation")
    setLicenses("Apache-2.0")
    publicDownloadNumbers = true
    version.apply {
      name = "$projectVersion"
      desc = "$projectDescription"
      released = Date().toString()
    }
  }
}
/* ***************************************************************************/

/* JVM attributes ************************************************************/
tasks.withType(KotlinCompile::class.java).all {
  kotlinOptions {
    jvmTarget = "$jvmTargetVersion"
  }
}
/* ***************************************************************************/
