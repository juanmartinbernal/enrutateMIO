plugins {
    alias(libs.plugins.enrutatemio.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.enrutatemio.core.network"
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    api(libs.retrofit.core)
    api(libs.retrofit.moshi)
    api(libs.okhttp.core)
    implementation(libs.okhttp.logging)
    api(libs.moshi.core)
    api(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)

    implementation(libs.koin.android)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
