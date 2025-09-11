plugins {
    `java-library`
    `maven-publish`
    kotlin("jvm")
}

group = "love.yinlin"
version = "4.4.31"

java {
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}

val copyNativeLibsTaskName = "copyNativeLibs"
tasks.register<Copy>(copyNativeLibsTaskName) {
    val osName = System.getProperty("os.name").lowercase()
    val osArch = System.getProperty("os.arch").lowercase()

    val targetPath = "src/main/resources/native/${
        when {
            osName.contains("mac") || osName.contains("darwin") -> "mac"
            osName.contains("windows") -> "windows"
            osName.contains("linux") -> "linux"
            else -> "unknown"
        }
    }/${
        when {
            osArch.contains("aarch64") || osArch.contains("arm64") -> "arm64"
            osArch.contains("arm") -> "arm"
            osArch.contains("x86_64") || osArch.contains("amd64") -> "x64"
            osArch.contains("x86") -> "x86"
            else -> "unknown"
        }
    }"

    from(".cxx") {
        // mac
        include("libpag4j.dylib")
        include("libEGL.dylib")
        include("libGLESv2.dylib")
        // windows
        include("pag4j.dll")
        include("libEGL.dll")
        include("libGLESv2.dll")
    }
    from(".cxx/libpag") {
        include("libpag.a")
        include("libpag.lib")
    }
    into(targetPath)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    doFirst {
        println("Copying native libraries to: $targetPath")
    }
}

tasks.jar {
    archiveBaseName.set("pag4j")
    from(sourceSets.main.get().output)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    dependsOn(copyNativeLibsTaskName)
}

tasks.named<ProcessResources>("processResources") {
    dependsOn(copyNativeLibsTaskName)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.register<Jar>("sourcesJar") {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    dependsOn(copyNativeLibsTaskName)
}

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
    dependsOn(tasks.javadoc)
    dependsOn(copyNativeLibsTaskName)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])

            pom {
                name = "pag4j"
                description = "Java binding for libpag"
                url = "https://github.com/vompom/pag-kmp/"

                licenses {
                    license {
                        name = "Apache License, Version 2.0"
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                    }
                }

                developers {
                    developer {
                        id = "libpag"
                        name = "libpag"
                        email = "libpag@tencent.com"
                    }
                    developer {
                        id = "ylcs"
                        name = "银临茶舍"
                    }
                }
            }
        }
    }
}
