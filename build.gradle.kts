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

val generatedResourcesDir = layout.buildDirectory.dir("generated/gantry-resources/main")

sourceSets {
    create("datagen") {
        kotlin.srcDirs("src/datagen/kotlin")
        resources.srcDirs("src/datagen/resources")

        compileClasspath += sourceSets["main"].compileClasspath + files(sourceSets["main"].output.classesDirs)
        runtimeClasspath += output + compileClasspath
    }
}

repositories {
    mavenLocal()
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
    compileOnly(kotlin("stdlib"))

    compileOnly("io.papermc.paper:paper-api:$minecraftVersion-R0.1-SNAPSHOT")
    compileOnly("io.github.pylonmc:rebar:$rebarVersion")
    compileOnly("io.github.pylonmc:pylon:$pylonVersion")
}

configurations {
    named("datagenImplementation") { extendsFrom(implementation.get()) }
    named("datagenCompileOnly") { extendsFrom(compileOnly.get()) }
    named("datagenRuntimeOnly") { extendsFrom(runtimeOnly.get()) }
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

val runDatagen = tasks.register<JavaExec>("runDatagen") {
    group = "datagen"
    description = "Generates resource files for Gantry"

    classpath = sourceSets["datagen"].runtimeClasspath
    mainClass = "me.glomdom.gantry.datagen.MainKt"

    val outDir = generatedResourcesDir.get().asFile
    outputs.dir(outDir)

    args(outDir.absolutePath)
}

sourceSets {
    main {
        resources.srcDir(runDatagen)
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