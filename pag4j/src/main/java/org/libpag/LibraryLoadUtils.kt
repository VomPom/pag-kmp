package org.libpag

import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.jvm.java

/**
 *
 * Created by @juliswang on 2025/07/25 21:26
 *
 * @Description
 */
internal object LibraryLoadUtils {
    private val loaded = AtomicBoolean(false)
    private val systemInfo by lazy { getDetailedSystemInfo() }

    @JvmStatic
    fun loadPag4j() {
        if (loaded.get()) return
        synchronized(this) {
            loadLibrary("libEGL")
            loadLibrary("libGLESv2")
            loadLibrary("libpag4j")
            loaded.set(true)
        }
    }

    @Suppress("UnsafeDynamicallyLoadedCode")
    @JvmStatic
    fun loadLibrary(libName: String) {
        val resourcePath = r(libName)
        val inputStream = LibraryLoadUtils::class.java.getResourceAsStream(resourcePath)
        if (inputStream == null) {
            println("Failed to find native library: $libName")
            return
        }

        // 提取到临时目录
        val tempDir = Files.createTempDirectory("natives").toFile()
        tempDir.deleteOnExit()
        val tempFile = File(tempDir, libName)
        inputStream.use { input ->
            Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        println("load native library in:${tempFile.absolutePath}")
        System.load(tempFile.absolutePath)
    }

    data class SystemInfo(
        val osType: String,  // "mac", "linux", "windows"
        val cpuArch: String, // "arm64", "x64", "unknown"
        val isMac: Boolean,
        val isArm64: Boolean,
        val isX64: Boolean,
        val suffix: String
    )

    private fun r(lib: String) = "/native/${systemInfo.osType}/${systemInfo.cpuArch}/$lib.${systemInfo.suffix}"

    fun getDetailedSystemInfo(): SystemInfo {
        val osName = System.getProperty("os.name").lowercase()
        val osArch = System.getProperty("os.arch").lowercase()

        return SystemInfo(
            osType = when {
                osName.contains("mac") || osName.contains("darwin") -> "mac"
                osName.contains("linux") -> "linux"
                osName.contains("win") -> "windows"
                else -> "unknown"
            },
            cpuArch = when (osArch) {
                "aarch64", "arm64" -> "arm64"
                "x86_64" -> "x64"
                else -> "unknown"
            },
            isMac = osName.contains("mac") || osName.contains("darwin"),
            isArm64 = osArch == "aarch64" || osArch == "arm64",
            isX64 = osArch == "x86_64",
            suffix = when {
                osName.contains("mac") || osName.contains("darwin") -> "dylib"
                osName.contains("win") -> "dll"
                else -> "so"
            }
        )
    }


}