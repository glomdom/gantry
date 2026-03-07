import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription
import org.jetbrains.kotlin.gradle.dsl.JvmDefaultMode

plugins {
    idea

    kotlin("jvm") version "2.3.0"

    id("com.gradleup.shadow") version "9.0.0"
    id("de.eldoria.plugin-yml.paper") version "0.7.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
}

group = "me.glomdom"
version = "1.0-SNAPSHOT"

val minecraftVersion = property("minecraft.version").toString()
val rebarVersion = property("rebar.version").toString()
val pylonVersion = property("pylon.version").toString()

repositories {
    mavenCentral()

    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc"
    }

    maven("https://jitpack.io") {
        name = "JitPack"
    }

    maven("https://repo.xenondevs.xyz/releases") {
        name = "InvUI"
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    compileOnly("io.github.pylonmc:rebar:$rebarVersion")
    compileOnly("io.github.pylonmc:pylon:$pylonVersion")
}

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}

kotlin {
    jvmToolchain(21)

    compilerOptions {
        javaParameters = true
        jvmDefault = JvmDefaultMode.NO_COMPATIBILITY
        freeCompilerArgs = listOf("-Xwhen-guards")
    }
}

tasks.shadowJar {
    mergeServiceFiles()

    archiveBaseName = "gantry"
    archiveClassifier = null
}

paper {
    generateLibrariesJson = true

    serverDependencies {
        register("Rebar") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }

        register("Pylon") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }

    name = "Gantry"
    main = "me.glomdom.gantry.Gantry"
    version = project.version.toString()
    authors = listOf("glomdom")
    apiVersion = minecraftVersion
    load = BukkitPluginDescription.PluginLoadOrder.STARTUP
}

tasks.runServer {
    downloadPlugins {
        github("pylonmc", "rebar", rebarVersion, "rebar-$rebarVersion.jar")
        github("pylonmc", "pylon", pylonVersion, "pylon-$pylonVersion.jar")
    }

    maxHeapSize = "4G"
    minecraftVersion(minecraftVersion)
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}