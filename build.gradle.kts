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

dependencies {
    implementation("org.openjfx:javafx-base:21:$platform")
    implementation("org.openjfx:javafx-graphics:21.0.2:$platform")
    implementation("org.openjfx:javafx-controls:21:$platform")
    implementation("org.openjfx:javafx-fxml:21:$platform")

    implementation("org.controlsfx:controlsfx:11.2.0")

    implementation("org.apache.poi:poi-ooxml:5.5.0")
    implementation("org.apache.logging.log4j:log4j-core:2.24.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:3.25.3")
}

application {
    mainModule.set("org.example.dutymanger_demo_first")
    mainClass.set("org.example.dutymanger_demo_first.Launcher")
}

jlink {
    imageName = "DutyManagerApp"

    launcher {
        name = "dutymanager"
    }

    jpackage {
        installerType = "dmg"
        appVersion = "1.0.0"

        installerOptions = listOf(
            "--mac-package-identifier", "org.example.dutymanager.demo",
            "--mac-package-name", "DutyManager"
        )

        jvmArgs.add("--enable-native-access=ALL-UNNAMED")
    }

    options.set(listOf(
        "--strip-debug",
        "--compress", "2",
        "--no-header-files",
        "--no-man-pages"
    ))
    addOptions("--bind-services")
}