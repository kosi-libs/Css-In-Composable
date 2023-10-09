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
    // NOT USED - Workaround to make Dokka happy in MPP JS Only projects
    // https://github.com/Kotlin/dokka/issues/3122
    jvm()
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