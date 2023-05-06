
plugins {
    kotlin("jvm") version "1.7.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    kotlin("kapt") version "1.7.10"
}

group = "co.pvphub.blossom"
version = ""

repositories {
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots")
    }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://maven.pvphub.me/releases")
    }
    maven {
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    // General dependencies
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.7.10")

    // Proxy dependencies
    compileOnly("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    compileOnly("io.netty:netty-all:4.0.20.Final")
    kapt("com.velocitypowered:velocity-api:3.1.2-SNAPSHOT")
    implementation("co.pvphub:VelocityUtils:-SNAPSHOT")
    implementation(project(":common"))
    implementation("me.carleslc.Simple-YAML:Simple-Yaml:1.7.2")
}

sourceSets["main"].resources.srcDir("src/resources/")

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set("blossom-velocity")
        mergeServiceFiles()
    }
}