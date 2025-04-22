import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktorVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "2.1.20"
}

group = "de.fridolin1"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-simple
    implementation("org.slf4j:slf4j-simple:2.0.17")
    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-core
    implementation("com.twelvemonkeys.imageio:imageio-core:3.12.0")
    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-bmp
    implementation("com.twelvemonkeys.imageio:imageio-bmp:3.12.0")
    // https://mvnrepository.com/artifact/com.twelvemonkeys.imageio/imageio-webp
    implementation("com.twelvemonkeys.imageio:imageio-webp:3.12.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "PhyPwdMng"
            packageVersion = "1.0.0"
        }
    }
}
