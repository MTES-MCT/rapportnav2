package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.FileInputStream
import java.nio.file.Path
import java.util.zip.ZipInputStream

class FileUtilsTest {

    private lateinit var fileUtils: FileUtils

    @BeforeEach
    fun setUp() {
        fileUtils = FileUtils()
    }

    @Test
    fun `should zip files correctly`(@TempDir tempDir: Path) {
        // Arrange
        val file1 = createTempFileWithContent(tempDir, "file1.txt", "Hello, world!")
        val file2 = createTempFileWithContent(tempDir, "file2.txt", "Kotlin is great!")

        val outputZipFile = tempDir.resolve("output.zip").toFile()

        // Act
        fileUtils.zip(outputZipFile, listOf(file1, file2))

        // Assert
        assertTrue(outputZipFile.exists(), "The output zip file should exist")

        val zippedFiles = getZippedFiles(outputZipFile)
        assertEquals(2, zippedFiles.size, "There should be 2 files in the zip")

        assertEquals("Hello, world!", zippedFiles["file1.txt"], "Content of file1.txt should match")
        assertEquals("Kotlin is great!", zippedFiles["file2.txt"], "Content of file2.txt should match")
    }

    private fun createTempFileWithContent(tempDir: Path, filename: String, content: String): File {
        val file = tempDir.resolve(filename).toFile()
        file.writeText(content)
        return file
    }

    private fun getZippedFiles(zipFile: File): Map<String, String> {
        val contentMap = mutableMapOf<String, String>()
        ZipInputStream(FileInputStream(zipFile)).use { zipIn ->
            var entry = zipIn.nextEntry
            while (entry != null) {
                val content = zipIn.readBytes().toString(Charsets.UTF_8)
                contentMap[entry.name] = content
                zipIn.closeEntry()
                entry = zipIn.nextEntry
            }
        }
        return contentMap
    }
}
