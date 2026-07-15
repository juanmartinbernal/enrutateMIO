plugins {
    alias(libs.plugins.enrutatemio.android.library)
    alias(libs.plugins.enrutatemio.android.library.compose)
}

android {
    namespace = "com.enrutatemio.core.designsystem"
}

dependencies {
    api(project(":core:model"))
    implementation(libs.androidx.core.ktx)
}
