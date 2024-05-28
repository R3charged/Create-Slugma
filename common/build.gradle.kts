plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}


dependencies {
    minecraft("com.mojang:minecraft:1.20.1")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader_version")}")
    modCompileOnly("com.cobblemon:mod:1.5.1+1.20.1-SNAPSHOT") {
        isTransitive = false
    }
    modCompileOnly("com.simibubi.create:create-fabric-${project.property("minecraft_version")}:${project.property("create_fabric_version")}")

    // required for proper remapping and compiling
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}
sourceSets.main {
    resources { // include generated resources in resources
        srcDir("src/generated/resources")
        exclude(".cache/**")
        exclude("assets/create/**")
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}