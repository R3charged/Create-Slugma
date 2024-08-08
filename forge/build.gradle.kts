plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

architectury {
    platformSetupLoomIde()
    forge()
}

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentForge").extendsFrom(configurations["common"])
}

loom {

    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    forge {
        mixinConfig("createslugma-common.mixins.json")
    }
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    // Forge
    forge("net.minecraftforge:forge:${property("forge_version")}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":common", "transformProductionForge")) { isTransitive = false }

    modImplementation("com.cobblemon:forge:${property("cobblemon_version")}")

    modImplementation("com.simibubi.create:create-${property("minecraft_version")}:${property("create_forge_version")}:slim") { isTransitive = false }
    modImplementation("com.tterrag.registrate:Registrate:${property("registrate_forge_version")}")
    modImplementation("com.jozufozu.flywheel:flywheel-forge-${property("minecraft_version")}:${property("flywheel_forge_version")}")

    implementation("thedarkcolour:kotlinforforge:4.4.0")



}

tasks.processResources {
    filesMatching("META-INF/mods.toml") {
        expand(
            mapOf(
                "author" to project.property("author"),
                "mod_name" to project.property("mod_name"),
                "mod_id" to project.property("mod_id"),
                "version" to project.property("mod_version"),
                "mod_description" to project.property("mod_description"),
                "repository" to project.property("repository"),
                "license" to project.property("license"),
                "mod_icon" to project.property("mod_icon"),
                "environment" to project.property("environment"),
                "supported_minecraft_versions" to project.property("supported_minecraft_versions"),
                "create_forge_version" to project.property("create_forge_version")
            )
        )
    }
}

tasks {
    base.archivesName.set("${project.property("archives_base_name")}-forge")
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("generations/gg/generations/core/generationscore/forge/datagen/**")
        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar.get().archiveClassifier.set("dev")
}