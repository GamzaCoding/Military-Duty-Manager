@file:Suppress("DEPRECATION")

plugins {
    id("java")
    id("application")
    id("org.beryx.jlink") version "3.1.1"
}

repositories {
    mavenCentral()
}

val osName = System.getProperty("os.name").lowercase()
val arch = System.getProperty("os.arch").lowercase()

val platform = when {
    osName.contains("mac") && (arch.contains("aarch64") || arch.contains("arm64")) ->
        "mac-aarch64"
    osName.contains("mac") -> "mac"
    osName.contains("win") -> "win"
    arch.contains("aarch64") || arch.contains("arm64") -> "linux-aarch64"
    else -> "linux"
}

val javafxVersion = "21.0.2"

dependencies {
    implementation("org.openjfx:javafx-base:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-graphics:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-controls:$javafxVersion:$platform")
    implementation("org.openjfx:javafx-fxml:$javafxVersion:$platform")

    implementation("org.controlsfx:controlsfx:11.2.0")
    implementation("org.apache.poi:poi-ooxml:5.5.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")
    implementation("commons-io:commons-io:2.15.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

application {
    mainModule.set("org.example.dutymanager")
    mainClass.set("org.example.dutymanager.Launcher")
}

jlink {
    imageName = "DutyManagerApp"

    launcher {
        name = "dutymanager"
    }

    jpackage {
        appVersion = "1.0.0"

        if (System.getProperty("os.name").lowercase().contains("mac")) {
            installerType = "dmg"
            installerOptions = listOf(
                "--mac-package-identifier", "org.example.dutymanager",
                "--mac-package-name", "DutyManager"
            )
        } else if (System.getProperty("os.name").lowercase().contains("win")) {
            installerType = "exe"
            installerOptions = listOf(
                "--win-menu",
                "--win-shortcut"
            )
        }
        jvmArgs.add("--enable-native-access=ALL-UNNAMED")
    }
    forceMerge("poi-ooxml")
    forceMerge("log4j-core")

    options.set(listOf(
        "--strip-debug",
        "--no-header-files",
        "--no-man-pages"
    ))
    addOptions("--bind-services")
}

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}