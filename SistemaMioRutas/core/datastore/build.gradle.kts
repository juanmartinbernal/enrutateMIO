plugins {
    alias(libs.plugins.enrutatemio.android.library)
}

android {
    namespace = "com.enrutatemio.core.datastore"
}

dependencies {
    implementation(project(":core:common"))
    api(libs.androidx.datastore.preferences)
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
