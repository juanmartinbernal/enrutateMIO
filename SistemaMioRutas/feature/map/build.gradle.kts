plugins {
    alias(libs.plugins.enrutatemio.android.feature)
}

android {
    namespace = "com.enrutatemio.feature.map"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)
    implementation(libs.maps.ktx.utils)
    implementation(libs.accompanist.permissions)
}
