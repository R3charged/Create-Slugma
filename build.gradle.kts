plugins {
    id("java")
    id("java-library")
    kotlin("jvm") version("1.7.10")

    id("dev.architectury.loom") version("1.5-SNAPSHOT") apply false
    id("architectury-plugin") version("3.4-SNAPSHOT") apply false
}

group = "com.cobblemon.mdks"
version = "1.0.0-SNAPSHOT"


allprojects {
    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    repositories {
        mavenCentral()
        maven(url = "https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/")
        maven("https://maven.impactdev.net/repository/development/")
        maven (url = "https://api.modrinth.com/maven") // LazyDFU
        maven (url = "https://maven.terraformersmc.com/releases/") // Mod Menu
        maven (url = "https://mvn.devos.one/snapshots/") // Create Fabric, Porting Lib, Forge Tags, Milk Lib, Registrate Fabric
        maven (url = "https://mvn.devos.one/releases/") // Porting Lib Releases
        maven (url = "https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge Config API Port
        maven (url = "https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
        maven (url = "https://jitpack.io/") // Mixin Extras, Fabric ASM
        maven(url = "https://maven.tterrag.com/") {
            content {
                includeGroup("com.jozufozu.flywheel")
            }
        }
    }

    tasks.getByName<Test>("test") {
        useJUnitPlatform()
    }
}

