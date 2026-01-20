package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ZipFiles
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
        val file1 = MissionExportEntity(fileName = "file1.txt", fileContent = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==")
        val file2 = MissionExportEntity(fileName = "file2.txt", fileContent = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==")
        val files = listOf(file1, file2)

        try {
            val base64Result = zipFiles.execute(files)

            assertNotNull(base64Result)
            assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
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


}
