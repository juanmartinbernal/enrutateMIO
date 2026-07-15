plugins {
    alias(libs.plugins.enrutatemio.android.library)
    alias(libs.plugins.enrutatemio.android.library.compose)
}

android {
    namespace = "com.enrutatemio.core.ui"
}

dependencies {
    api(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
}
