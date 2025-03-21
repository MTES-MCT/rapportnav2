package fr.gouv.gmampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.io.File
import java.io.FileInputStream
import java.util.zip.ZipInputStream

@SpringBootTest(classes = [FileUtils::class])
class ExportZipTest {

    @Autowired
    private lateinit var fileUtils: FileUtils

    @MockitoBean
    private lateinit var fillAEMExcelRow: FillAEMExcelRow

    @Test
    fun `should zip a list of files`() {

        val filesToZip = listOf(
            File("file1.txt").apply { writeText("Hello, World!") },
            File("file2.txt").apply { writeText("Kotlin ZIP Test") })

        val zipFile = File("test_output.zip")

        val outputFile = fileUtils.zip(zipFile, filesToZip)

        val filesInZip = mutableListOf<String>()
        ZipInputStream(FileInputStream(zipFile)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                filesInZip.add(entry.name)
                entry = zis.nextEntry
            }
        }

        assertTrue(outputFile.exists())
        assertTrue(outputFile.length() > 0)
        assertEquals(2, filesInZip.size)
        assertTrue(filesInZip.contains("file1.txt"))
        assertTrue(filesInZip.contains("file2.txt"))

        zipFile.delete()
        for (file in filesToZip) {
            file.delete()
        }

    }
}
