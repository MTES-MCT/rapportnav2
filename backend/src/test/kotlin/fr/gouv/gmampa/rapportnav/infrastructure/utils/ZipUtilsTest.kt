package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.infrastructure.utils.ZipUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class ZipUtilsTest {

    @Test
    fun `should zip multiple files and return base64 representation`() {
        val file1 = MissionExportEntity(fileName = "file1.txt", fileContent = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==")
        val file2 = MissionExportEntity(fileName = "file2.txt", fileContent = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==")
        val files = listOf(file1, file2)

        val base64Result = ZipUtils.zipToBase64(files)

        assertNotNull(base64Result)
        assertTrue(Base64.getDecoder().decode(base64Result).isNotEmpty())
    }

    @Test
    fun `should throw exception for empty file list`() {
        val exception = assertThrows<BackendUsageException> {
            ZipUtils.zipToBase64(emptyList())
        }
        assertEquals("File list cannot be empty", exception.message)
        assertEquals(BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION, exception.code)
    }
}
