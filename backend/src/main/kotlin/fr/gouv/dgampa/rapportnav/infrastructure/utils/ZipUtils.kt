package fr.gouv.dgampa.rapportnav.infrastructure.utils

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipUtils {

    private val logger = LoggerFactory.getLogger(ZipUtils::class.java)

    /**
     * Zip files together and return its base64 representation
     *
     * @param files a list of files
     * @return The base64 encoded representation of a zip
     */
    fun zipToBase64(files: List<MissionExportEntity>): String {
        logger.info("ZipUtils - zipping ${files.size} files")

        if (files.isEmpty()) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "File list cannot be empty"
            )
        }

        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            ZipOutputStream(byteArrayOutputStream).use { zipOut ->
                files.forEach { fileEntity ->
                    // Validate filename to prevent path traversal attacks
                    val fileName = fileEntity.fileName
                    if (fileName.contains("..") || fileName.contains("/") || fileName.contains("\\")) {
                        logger.error("Invalid filename detected - potential path traversal: {}", fileName)
                        throw BackendUsageException(
                            code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                            message = "Invalid filename: contains path traversal characters"
                        )
                    }

                    val decodedBytes = Base64.getDecoder().decode(fileEntity.fileContent)
                    zipOut.putNextEntry(ZipEntry(fileName))
                    ByteArrayInputStream(decodedBytes).use { it.copyTo(zipOut) }
                    zipOut.closeEntry()
                }
            }
            return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray())
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to zip files: ${e.message}",
                originalException = e
            )
        }
    }
}
