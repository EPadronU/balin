/* Imports *******************************************************************/
import com.jfrog.bintray.gradle.BintrayExtension
import io.qameta.allure.gradle.AllureExtension
import io.qameta.allure.gradle.config.TestNGConfig
import java.util.Date
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/* ***************************************************************************/

/* Properties ****************************************************************/
val allureVersion by project
val htmlUnitDriverVersion by project
val jvmTargetVersion by project
val kotlinStdLib by project
val projectDescription by project
val projectVersion by project
val seleniumVersion by project
val slf4jVersion by project
val testNgVersion by project
/* ***************************************************************************/

/* Build script's setup ******************************************************/
buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.0")
    classpath("io.qameta.allure:allure-gradle:2.5")
  }
}
/* ***************************************************************************/

/* Plugins *******************************************************************/
plugins {
  kotlin("jvm") version "1.2.30"
}

apply {
  plugin("com.jfrog.bintray")
  plugin("io.qameta.allure")
  plugin("maven-publish")
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

  testCompile("org.testng:testng:$testNgVersion")
  testCompile("org.seleniumhq.selenium:htmlunit-driver:$htmlUnitDriverVersion")
}

repositories {
  jcenter()
}
/* ***************************************************************************/

/* Allure's setup **********************************************************/
configure<AllureExtension> {
  autoconfigure = false

  version = "$allureVersion"

  useTestNG = closureOf<TestNGConfig> {
    version = "$allureVersion"
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

/* Test task configuration ***************************************************/
tasks.withType(Test::class.java).all {
  ignoreFailures = true

  useTestNG()
}
/* ***************************************************************************/

/* JVM attributes ************************************************************/
tasks.withType(KotlinCompile::class.java).all {
  kotlinOptions {
    jvmTarget = "$jvmTargetVersion"
  }
}
/* ***************************************************************************/
