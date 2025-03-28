plugins {
    kotlin("jvm") version "2.1.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    //id("io.papermc.paperweight.userdev") version "1.7.4"
    id("maven-publish")
}

group = "kr.apo2073"
version = "1.2.1"

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["java"])
                groupId = "com.github.apo2073"
                artifactId = "MMOAddon"
                version = version

                pom {
                    name.set("MMOAddon")
                    description.set("")
                }
            }
        }
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    maven("https://nexus.phoenixdevt.fr/repository/maven-public/") {
        name="phoenix"
    }
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://repo.skriptlang.org/releases")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    //paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
    //paperweightDevBundle("io.papermc.paper:dev-bundle:1.20.1-R0.1-SNAPSHOT")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.lumine:MythicLib-dist:1.7.1-SNAPSHOT")
    compileOnly("io.lumine:Mythic-Dist:5.4.0")
    //implementation("net.Indyuce:MMOItems-API:6.9.4-SNAPSHOT")
    implementation("net.Indyuce:MMOItems-API:6.10-SNAPSHOT")
    compileOnly("net.Indyuce:MMOCore-API:1.13.1-SNAPSHOT")

    compileOnly("me.clip:placeholderapi:2.11.6")

    implementation("com.github.apo2073:ApoLib:1.0.4")
    implementation("com.github.SkriptLang:Skript:2.9.3")

//    implementation("net.wesjd:anvilgui:1.10.4-SNAPSHOT")
}

val targetJavaVersion = 17
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}
//
//tasks.shadowJar {
//    relocate("net.wesjd.anvilgui", "kr.apo2073.mmoAddon.anvilgui")
//    minimize()
//}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("plugin.yml") {
        expand(props)
    }
}