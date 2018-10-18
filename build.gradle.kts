/* Imports *******************************************************************/
import com.jfrog.bintray.gradle.BintrayExtension
import io.qameta.allure.gradle.AllureExtension
import io.qameta.allure.gradle.config.TestNGConfig
import java.util.Date
import java.net.URL
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.DokkaConfiguration.ExternalDocumentationLink
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/* ***************************************************************************/

/* Properties ****************************************************************/
val allureVersion: String by project
val htmlUnitDriverVersion: String by project
val jvmTargetVersion: String by project
val kotlinStdLib: String by project
val logbackVersion: String by project
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
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
        classpath("io.qameta.allure:allure-gradle:2.5")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.9.17")
    }
}
/* ***************************************************************************/

/* Plugins *******************************************************************/
plugins {
    kotlin("jvm") version "1.2.71"
    id("io.gitlab.arturbosch.detekt") version "1.0.0.RC9.2"
}

apply {
    plugin("com.jfrog.bintray")
    plugin("io.qameta.allure")
    plugin("maven")
    plugin("maven-publish")
    plugin("org.jetbrains.dokka")
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

    testRuntime("ch.qos.logback:logback-classic:$logbackVersion")
}

repositories {
    jcenter()
    mavenCentral()
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
    toolVersion = "1.0.0.RC9.2"
    input = files("src/main/kotlin")
    filters = ".*/resources/.*"
}
/* ***************************************************************************/

/* Publication setup *********************************************************/
val sourcesJarTask = task<Jar>("sourceJar") {
    from(sourceSets["main"].allSource)

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

/* Dokka configuration *******************************************************/
tasks.withType(DokkaTask::class.java).all {
    outputFormat = "javadoc"

    samples = listOf("$projectDir/src/test/kotlin")

    externalDocumentationLink(delegateClosureOf<ExternalDocumentationLink.Builder> {
        url = URL("https://seleniumhq.github.io/selenium/docs/api/java/index.html")
    })
}
/* ***************************************************************************/

/* JVM attributes ************************************************************/
tasks.withType(KotlinCompile::class.java).all {
    kotlinOptions {
        jvmTarget = jvmTargetVersion
    }
}
/* ***************************************************************************/
