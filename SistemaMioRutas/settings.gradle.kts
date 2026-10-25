pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SistemaMioRutas"

include(":app")

include(":core:common")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:ui")

include(":domain")
include(":data")

include(":feature:routes")
include(":feature:feeders")
include(":feature:recharge")
include(":feature:map")
include(":feature:news")
include(":feature:about")
include(":feature:integration")
