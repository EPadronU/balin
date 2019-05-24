/* Imports *******************************************************************/
import java.util.Date
import java.net.URL
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.jetbrains.dokka.DokkaConfiguration.ExternalDocumentationLink
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
/* ***************************************************************************/

/* Properties ****************************************************************/
val allureVersion: String by project
val detektVersion: String by project
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

/* Plugins *******************************************************************/
plugins {
    kotlin("jvm").version("1.3.21")
    id("com.jfrog.bintray").version("1.8.4")
    id("io.gitlab.arturbosch.detekt").version("1.0.0-RC14")
    id("io.qameta.allure").version("2.7.0")
    id("maven")
    id("maven-publish")
    id("org.jetbrains.dokka").version("0.9.18")
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
allure {
    autoconfigure = false

    version = allureVersion

    useTestNG {
        version = allureVersion
    }
}
/* ***************************************************************************/

/* Detekt's setup ************************************************************/
detekt {
    toolVersion = detektVersion

    input = files("src/main/kotlin")

    filters = ".*/resources/.*"
}
/* ***************************************************************************/

/* Publication setup *********************************************************/
val sourcesJarTask = task<Jar>("sourceJar") {
    from(sourceSets["main"].allSource)

    classifier = "sources"
}

publishing {
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

bintray {
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

    useTestNG() {
        suites("src/test/resources/testng.xml")
    }
}
/* ***************************************************************************/

/* Dokka configuration *******************************************************/
val dokka by tasks.getting(DokkaTask::class) {
    outputFormat = "html"

    outputDirectory = "$projectDir/docs/kotlin/api"
}

task("dokkajdoc", DokkaTask::class) {
    outputFormat = "javadoc"

    outputDirectory = "$projectDir/docs/java/api"
}

tasks.withType(DokkaTask::class.java).all {
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
