

plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    id("io.papermc.paperweight.userdev") version "1.5.3"
}

group = "co.pvphub.blossom"
version = ""

repositories {
    mavenCentral()
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    // General dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")

    // Backend dependencies
    paperweight.paperDevBundle("1.19.2-R0.1-SNAPSHOT")
    implementation(project(":common"))
}

sourceSets["main"].resources.srcDir("src/resources/")

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set("blossom-backend")
        mergeServiceFiles()
    }
}