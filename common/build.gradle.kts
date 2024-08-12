plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    common("forge", "fabric")
    platformSetupLoomIde()
}

loom {
    accessWidenerPath.set(project.file("src/main/resources/createslugma.accesswidener"))
    mixin {
        defaultRefmapName = "${property("mod_id")}.refmap.json"
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modCompileOnly("com.cobblemon:mod:${property("cobblemon_version")}")
    modCompileOnly("com.simibubi.create:create-fabric-${property("minecraft_version")}:${property("create_fabric_version")}")
    // all fabric dependencies:
    modCompileOnly("net.fabricmc:fabric-loader:${property("fabric_loader_version")}")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    implementation("com.bedrockk:molang:1.1.11")
}


sourceSets.main {
    resources { // include generated resources in resources
        srcDir("src/generated/resources")
        exclude(".cache/**")
    }
}
