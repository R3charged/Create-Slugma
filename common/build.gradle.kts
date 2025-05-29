plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

val enableFabric = (findProperty("enableFabric") as? String)?.toBoolean() ?: false

architectury {
    common("${property("enabled_platforms")}".split(","))
    platformSetupLoomIde()
}

loom {
    accessWidenerPath.set(project.file("src/main/resources/createslugma.accesswidener"))
    mixin {
        defaultRefmapName = "${property("mod_id")}.refmap.json"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("base_minecraft_version")}")
    mappings(loom.officialMojangMappings())

    modCompileOnly("net.neoforged:neoforge:${property("neo_version")}")
    compileOnly("javax.annotation:javax.annotation-api:1.3.2")
    compileOnly("com.google.code.findbugs:jsr305:3.0.2")
    modCompileOnly("com.cobblemon:mod:${property("cobblemon_version")}+${property("base_minecraft_version")}")
    modCompileOnly("com.simibubi.create:create-${property("base_minecraft_version")}:${property("create_neo_version")}:slim") { isTransitive = false }
    modCompileOnly("net.createmod.ponder:Ponder-NeoForge-${property("base_minecraft_version")}:${property("ponder_neo_version")}")
    modCompileOnly("com.tterrag.registrate:Registrate:${property("registrate_neo_version")}")
    modCompileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("base_minecraft_version")}:${property("flywheel_neo_version")}")

    if (enableFabric) {
        modCompileOnly("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
        modCompileOnly("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    }

    implementation("com.bedrockk:molang:1.1.11")
}


sourceSets.main {
    resources { // include generated resources in resources
        srcDir("src/generated/resources")
        exclude(".cache/**")
    }
}
