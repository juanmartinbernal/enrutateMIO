plugins {
    alias(libs.plugins.enrutatemio.android.feature)
}

android {
    namespace = "com.enrutatemio.feature.routes"
}

dependencies {
    implementation(project(":feature:recharge"))
}
