import org.jetbrains.kotlin.com.google.gson.JsonParser
import java.io.InputStreamReader
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

plugins {
    kotlin("jvm") version "1.8.0"
//    id("io.papermc.paperweight.userdev") version "1.4.0"
}

group = "com.github.curur"
version = "0.0.1"
val mcversion = properties["mcversion"]

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
//    paperDevBundle("1.19.4-R0.1-SNAPSHOT")
}

tasks {
    build {
        doLast {
            val serverFolder = File("D:/server/dev server/$mcversion")
            if (!serverFolder.exists()) {
                serverFolder.mkdir()

                // paper.jar
                val url = URL("https://api.papermc.io/v2/projects/paper/versions/$mcversion")
                val inputReader = InputStreamReader(url.openStream())
                val builds = JsonParser.parseReader(inputReader).asJsonObject.get("builds").asJsonArray
                val latestBuild = builds.get(builds.size() - 1).asString //제일 마지막 빌드

                val downloadUrl = URL(
                    "https://api.papermc.io/v2/projects/paper/versions/" +
                            "$mcversion/builds/$latestBuild/downloads/paper-$mcversion-$latestBuild.jar"
                )

                val path = Path.of(File("$serverFolder/paper.jar").toURI())
                downloadUrl.openStream().use {
                    Files.copy(it, path)
                }

                inputReader.close()

                //start.bat
                File(serverFolder, "start.bat")
                    .writeText("java -Xms128M -Xmx2G -jar paper.jar nogui")

            }
            copy {
                val jarName = "${rootProject.name}-$version.jar"
                from("build\\libs\\$jarName")
                into("$serverFolder\\plugins")
            }
            delete("$serverFolder\\plugins\\update\\RELOAD")
        }
    }
    assemble {
//        dependsOn(reobfJar)
    }
}
