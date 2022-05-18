plugins {
    kotlin("multiplatform") version "1.6.21"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev686"
    `maven-publish`
}

group = "org.kodein.cic"
version = "1.0.1"

allprojects {
    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }
}

kotlin {
    explicitApi()

    js(IR) {
        browser()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.svg)
                implementation(compose.runtime)
            }
        }

        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
                optIn("org.jetbrains.compose.web.ExperimentalComposeWebApi")
                optIn("kotlin.time.ExperimentalTime")
            }
        }
    }
}
