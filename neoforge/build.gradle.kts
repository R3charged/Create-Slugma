plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

configurations {
    create("common")
    create("shadowCommon")
    compileClasspath.get().extendsFrom(configurations["common"])
    runtimeClasspath.get().extendsFrom(configurations["common"])
    getByName("developmentNeoForge").extendsFrom(configurations["common"])
}

loom {

    enableTransitiveAccessWideners.set(true)
    silentMojangMappingsLicense()

    val common = project(":common")


    runs {
        create("datagen") {
            client()
            name = "Data Generation"
            runDir = "run"

            programArgs("--mod", "createslugma")
            programArgs("--all")
            programArgs("--output", file("${common.file("src/generated/resources")}").absolutePath)
            programArgs("--existing", file("${common.file("src/main/resources")}").absolutePath)

            source(sourceSets["main"])
        }

    }
}

dependencies {
    minecraft("net.minecraft:minecraft:${property("base_minecraft_version")}")
    mappings(loom.officialMojangMappings())

    // Forge
    neoForge("net.neoforged:neoforge:${property("neo_version")}")

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowCommon"(project(":common", "transformProductionForge")) { isTransitive = false }

    modImplementation("com.cobblemon:neoforge:${property("cobblemon_version")}+${property("base_minecraft_version")}")

    modImplementation("com.simibubi.create:create-${property("base_minecraft_version")}:${property("create_neo_version")}:slim") { isTransitive = false }
    modImplementation("net.createmod.ponder:Ponder-NeoForge-${property("base_minecraft_version")}:${property("ponder_neo_version")}")
    modImplementation("com.tterrag.registrate:Registrate:${property("registrate_neo_version")}")
    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("base_minecraft_version")}:${property("flywheel_neo_version")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("base_minecraft_version")}:${property("flywheel_neo_version")}")

    implementation("thedarkcolour:kotlinforforge-neoforge:${property("kotlinforforge_version")}") {
        exclude("net.neoforged.fancymodloader", "loader")
    }



}

tasks.processResources {
    filesMatching("META-INF/neoforge.mods.toml") {
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
                "create_neo_version" to project.property("create_neo_version"),
                "cobblemon_version" to project.property("cobblemon_version"),
                "minecraft_version" to project.property("base_minecraft_version"),
                "neo_version" to project.property("neo_version"),
                "loader_version_range" to project.property("loader_version_range")
            )
        )
    }
}




tasks {
    base.archivesName.set("${project.property("archives_base_name")}-neoforge")
    processResources {
        inputs.property("version", project.version)

        filesMatching("META-INF/neoforge.mods.toml") {
            expand(mapOf("version" to project.version))
        }
    }

    shadowJar {
        exclude("fabric.mod.json")
        exclude("generations/gg/generations/core/generationscore/neoforge/datagen/**")
        configurations = listOf(project.configurations.getByName("shadowCommon"))
        archiveClassifier.set("dev-shadow")
    }

    remapJar {
        inputFile.set(shadowJar.get().archiveFile)
        dependsOn(shadowJar)
    }

    jar.get().archiveClassifier.set("dev")
}