plugins {
    alias(libs.plugins.enrutatemio.android.feature)
}

android {
    namespace = "com.enrutatemio.feature.feeders"
}

dependencies {
    implementation(project(":core:datastore"))
    implementation(project(":feature:routes"))
}
