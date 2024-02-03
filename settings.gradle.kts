buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://raw.githubusercontent.com/kosi-libs/kodein-internal-gradle-plugin/mvn-repo")
    }
    dependencies {
        classpath("org.kodein.internal.gradle:kodein-internal-gradle-settings:8.5.1")
    }
}

apply { plugin("org.kodein.settings") }

rootProject.name = "Kosi-CiC"

include(
    "css-in-composable",
    "test"
)
