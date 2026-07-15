plugins {
    alias(libs.plugins.enrutatemio.android.library)
    alias(libs.plugins.enrutatemio.android.room)
}

android {
    namespace = "com.enrutatemio.core.database"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    androidTestImplementation(libs.androidx.test.ext.junit)
}
