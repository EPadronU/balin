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
val allureVersion: String by project
val htmlUnitDriverVersion: String by project
val jvmTargetVersion: String by project
val kotlinStdLib: String by project
val projectDescription: String by project
val projectGroup: String by project
val projectName: String by project
val projectRepository: String by project
val projectVersion: String by project
val seleniumVersion: String by project
val slf4jVersion: String by project
val testNgVersion: String by project
/* ***************************************************************************/

/* Build script's setup ******************************************************/
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.3")
        classpath("io.qameta.allure:allure-gradle:2.5")
    }
}
/* ***************************************************************************/

/* Plugins *******************************************************************/
plugins {
    kotlin("jvm") version "1.2.51"
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC7-3"
}

apply {
    plugin("com.jfrog.bintray")
    plugin("io.qameta.allure")
    plugin("maven")
    plugin("maven-publish")
}
/* ***************************************************************************/

/* Maven *********************************************************************/
group = projectGroup
version = projectVersion
/* ***************************************************************************/

/* Dependencies **************************************************************/
dependencies {
    compile(kotlin(kotlinStdLib))
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

    version = allureVersion

    useTestNG = closureOf<TestNGConfig> {
        version = allureVersion
    }
}
/* ***************************************************************************/

/* Detekt's setup ************************************************************/
detekt {
    version = "1.0.0.RC7-3"

    profile("main", Action {
        input = "src/main/kotlin"
        filters = ".*/resources/.*"
        output = "detekt-reports"
        outputName = "detekt-report"
        baseline = "reports/baseline.xml"
    })
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

            groupId = projectGroup
            artifactId = projectName
            version = projectVersion
        }
    }
}

configure<BintrayExtension> {
    user = System.getenv("JFROG_USER")
    key = System.getenv("JFROG_KEY")

    setPublications("JCenterPublication")

    pkg.apply {
        repo = "maven"
        name = projectName
        desc = projectDescription
        vcsUrl = projectRepository
        publish = true
        setLabels("kotlin", "selenium", "web", "geb", "automation")
        setLicenses("Apache-2.0")
        publicDownloadNumbers = true
        version.apply {
            name = projectVersion
            desc = projectDescription
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
        jvmTarget = jvmTargetVersion
    }
}
/* ***************************************************************************/
