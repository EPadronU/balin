/* Imports *******************************************************************/
import org.gradle.api.plugins.ExtensionAware
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
val seleniumVersion by project
val slf4jVersion by project
val spekVersion by project
/* ***************************************************************************/

/* Build script's setup ******************************************************/
buildscript {
  dependencies {
    classpath("com.kncept.junit5.reporter:junit-reporter:1.0.0")
    classpath("org.junit.platform:junit-platform-gradle-plugin:1.0.1")
  }
}
/* ***************************************************************************/

/* Plugins *******************************************************************/
plugins {
  kotlin("jvm")
}

apply {
  plugin("com.kncept.junit5.reporter")
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

/* ***************************************************************************/
tasks.withType(KotlinCompile::class.java).all {
  kotlinOptions {
    jvmTarget = "$jvmTargetVersion"
  }
}
/* ***************************************************************************/
