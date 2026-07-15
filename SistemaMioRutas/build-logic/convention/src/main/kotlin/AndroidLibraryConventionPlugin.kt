import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure(LibraryExtension::class.java) {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 35
                lint.targetSdk = 35

                testOptions.unitTests.isIncludeAndroidResources = true
            }
        }
    }
}
