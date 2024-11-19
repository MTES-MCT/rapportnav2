package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ZipFiles
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File
import java.util.*


class ZipFilesTest {

    private val fileUtils = FileUtils()
    private val base64Converter = Base64Converter()
    private val zipFiles = ZipFiles()

    @Test
    fun `should zip files and return base64 representation`() {
        // Arrange
        val testDir = File("testDir").apply { mkdir() }
        val file1 = File(testDir, "file1.txt").apply { writeText("Content 1") }
        val file2 = File(testDir, "file2.txt").apply { writeText("Content 2") }
        val files = listOf(file1, file2)

        try {
            // Act
            val base64Result = zipFiles.execute(files)

            // Assert
            assertNotNull(base64Result)
            assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
            assertFalse(file1.exists(), "file1 should have been deleted")
            assertFalse(file2.exists(), "file2 should have been deleted")
        } finally {
            // Cleanup
            testDir.deleteRecursively()
        }
    }
}
