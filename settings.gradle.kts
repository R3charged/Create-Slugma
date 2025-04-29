rootProject.name = "CreateSlugma"

pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        maven("https://maven.architectury.dev/")
        maven("https://maven.neoforged.net/releases")
        gradlePluginPortal()
    }
}

val enableFabric = providers.gradleProperty("enableFabric").forUseAtConfigurationTime().orNull == "true"

include(":common")
include(":neoforge")

if (enableFabric) {
    include(":fabric")
}