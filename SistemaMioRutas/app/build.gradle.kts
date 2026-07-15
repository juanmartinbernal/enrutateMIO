import java.util.Properties

plugins {
    alias(libs.plugins.enrutatemio.android.application)
    alias(libs.plugins.enrutatemio.android.application.compose)
    alias(libs.plugins.enrutatemio.koin)
}

// Lee credenciales locales desde local.properties (NUNCA se hardcodean en el código,
// a diferencia de la app legacy que tenía la API Key de Google Maps en el Manifest
// y las credenciales de Foursquare directamente en el código Java).
val secrets = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun secret(key: String, default: String = ""): String = secrets.getProperty(key) ?: default

android {
    namespace = "com.enrutatemio"

    defaultConfig {
        applicationId = "com.enrutatemio"
        versionCode = 21
        versionName = "4.0"

        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${secret("GOOGLE_MAPS_API_KEY")}\"")
        buildConfigField("String", "TWITTER_BEARER_TOKEN", "\"${secret("TWITTER_BEARER_TOKEN")}\"")
        buildConfigField(
            "String",
            "ENRUTATE_BACKEND_BASE_URL",
            "\"${secret("ENRUTATE_BACKEND_BASE_URL", "https://enrutatemio.com/")}\"",
        )

        manifestPlaceholders["googleMapsApiKey"] = secret("GOOGLE_MAPS_API_KEY")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))
    implementation(project(":core:datastore"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":domain"))
    implementation(project(":data"))

    implementation(project(":feature:routes"))
    implementation(project(":feature:feeders"))
    implementation(project(":feature:recharge"))
    implementation(project(":feature:map"))
    implementation(project(":feature:news"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.compose.material.icons.extended)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
