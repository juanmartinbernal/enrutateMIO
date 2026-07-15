plugins {
    alias(libs.plugins.enrutatemio.android.library)
}

android {
    namespace = "com.enrutatemio.core.common"
}

dependencies {
    api(project(":core:model"))

    api(libs.kotlinx.coroutines.core)
    api(libs.kotlinx.coroutines.android)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)

    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
