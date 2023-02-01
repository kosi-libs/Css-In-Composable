plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
        useCommonJs()
        binaries.executable()
    }

    sourceSets {
        named("jsMain") {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.svg)
                implementation(compose.runtime)

                implementation(rootProject)
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
