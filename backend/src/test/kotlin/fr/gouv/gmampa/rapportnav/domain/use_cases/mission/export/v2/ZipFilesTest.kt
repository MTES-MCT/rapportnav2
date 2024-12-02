package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ZipFiles
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.util.*

class ZipFilesTest {

    private val zipFiles = ZipFiles()

    @Test
    fun `should zip multiple files and return base64 representation`() {
        val testDir = File("testDir").apply { mkdir() }
        val file1 = File(testDir, "file1.txt").apply { writeText("Content 1") }
        val file2 = File(testDir, "file2.txt").apply { writeText("Content 2") }
        val files = listOf(file1, file2)

        try {
            val base64Result = zipFiles.execute(files)

            assertNotNull(base64Result)
            assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
            assertFalse(file1.exists(), "file1 should have been deleted")
            assertFalse(file2.exists(), "file2 should have been deleted")
        } finally {
            testDir.deleteRecursively()
        }
    }

    @Test
    fun `should handle a single file zipping`() {
        val testDir = File("testDir").apply { mkdir() }
        val file = File(testDir, "file1.txt").apply { writeText("Single file content") }
        val files = listOf(file)

        try {
            val base64Result = zipFiles.execute(files)

            assertNotNull(base64Result)
            assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
            assertFalse(file.exists(), "file should have been deleted")
        } finally {
            testDir.deleteRecursively()
        }
    }

    @Test
    fun `should throw exception for empty file list`() {
        val exception = assertThrows<IllegalArgumentException> {
            zipFiles.execute(emptyList())
        }
        assertEquals("File list cannot be empty", exception.message)
    }


    @Test
    fun `should handle large files zipping`() {
        val testDir = File("testDir").apply { mkdir() }
        val largeFile = File(testDir, "largeFile.txt").apply {
            writeText("A".repeat(10_000_000)) // 10MB file
        }
        val files = listOf(largeFile)

        try {
            val base64Result = zipFiles.execute(files)

            assertNotNull(base64Result)
            assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
            assertFalse(largeFile.exists(), "largeFile should have been deleted")
        } finally {
            testDir.deleteRecursively()
        }
    }


}
