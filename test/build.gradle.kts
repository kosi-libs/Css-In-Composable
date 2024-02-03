plugins {
    kodein.mpp
    alias(libs.plugins.compose)
}

kotlin.kodein {
    jsEnvBrowserOnly()
    js {
        sources.mainDependencies {
            implementation(kotlin.compose.html.core)
            implementation(kotlin.compose.runtime)
            implementation(projects.cssInComposable)
            implementation(libs.kotlinx.coroutinesCore)
        }
        target.binaries.executable()
    }
}

kotlin.sourceSets.all {
    languageSettings {
        optIn("org.jetbrains.compose.web.ExperimentalComposeWebApi")
        optIn("kotlin.time.ExperimentalTime")
    }
}
