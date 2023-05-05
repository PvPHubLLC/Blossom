import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("kapt") version "1.7.10"
}

group = "co.pvphub"
version = ""

repositories {
    mavenCentral()
}

dependencies {
}

sourceSets["main"].resources.srcDir("src/resources/")

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set("blossom")
        mergeServiceFiles()
    }
}