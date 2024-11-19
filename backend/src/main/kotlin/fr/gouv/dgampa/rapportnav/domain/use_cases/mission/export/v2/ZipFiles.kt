package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@UseCase
class ZipFiles {

    private val logger = LoggerFactory.getLogger(ZipFiles::class.java)

    /**
     * Zip files together and return its base64 representation
     *
     * @param files a list of files
     * @return The base64 encoded representation of a zip
     * @throws IOException if zipping or base64 conversion fails
     */
    fun execute(files: List<MissionExportEntity>): String {
        logger.info("ZipFiles - start")

        if (files.isEmpty()) {
            logger.error("No files provided for zipping")
            throw IllegalArgumentException("File list cannot be empty")
        }

        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            ZipOutputStream(byteArrayOutputStream).use { zipOut ->
                files.forEach { fileEntity ->
                    val decodedBytes = Base64.getDecoder().decode(fileEntity.fileContent)
                    val zipEntry = ZipEntry(fileEntity.fileName) // Use the fileName from the MissionExportEntity
                    zipOut.putNextEntry(zipEntry)

                    ByteArrayInputStream(decodedBytes).use { byteArrayInputStream ->
                        byteArrayInputStream.copyTo(zipOut)
                    }
                    zipOut.closeEntry()
                }
            }

            // Return the base64-encoded zip file content
            val base64Content = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
            return base64Content
        } catch (e: IOException) {
            logger.error("Error occurred during zipping or base64 conversion", e)
            throw IOException("Failed to zip files or convert to base64", e)
        } catch (e: Exception) {
            logger.error("Unexpected error during file processing", e)
            throw RuntimeException("Unexpected error in ZipFiles", e)
        }
    }
}
