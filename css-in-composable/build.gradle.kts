plugins {
    kodein.library.mpp
    alias(libs.plugins.compose)
}

kotlin.kodein {
    jsEnvBrowserOnly()
    js {
        sources.mainDependencies {
            implementation(kotlin.compose.html.core)
            implementation(kotlin.compose.runtime)
        }
    }
}

kotlin.sourceSets.all {
    languageSettings {
        optIn("org.jetbrains.compose.web.ExperimentalComposeWebApi")
        optIn("kotlin.time.ExperimentalTime")
    }
}

kodeinUpload {
    name = "Css-In-Composable"
    description = "Css classes generated in Compose-HTML composables"
}