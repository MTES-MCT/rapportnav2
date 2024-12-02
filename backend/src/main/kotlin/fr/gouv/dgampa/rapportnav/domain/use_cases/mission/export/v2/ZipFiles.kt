package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException

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
    fun execute(files: List<File>): String {
        logger.info("ZipFiles - start")

        if (files.isEmpty()) {
            logger.error("No files provided for zipping")
            throw IllegalArgumentException("File list cannot be empty")
        }

        val zipFile = File("tmp_output.zip")
        try {
            logger.info("Creating zip file from ${files.size} files")
            val fileUtils = FileUtils()
            val outputZipFile = fileUtils.zip(zipFile, files)

            logger.info("Converting zip file to base64")
            val base64Content = Base64Converter().convertToBase64(outputZipFile.absolutePath)

            logger.info("Deleting input files")
            for (file in files) {
                val isFileDeleted = file.delete()
                if (!isFileDeleted) {
                    logger.warn("Failed to delete file: ${file.name}")
                }
            }

            val isZipFileDeleted = outputZipFile.delete()
            if (!isZipFileDeleted) {
                logger.warn("Failed to delete temporary zip file: ${outputZipFile.name}")
            }

            logger.info("ZipFiles - completed successfully")
            return base64Content

        } catch (e: IOException) {
            logger.error("Error occurred during zipping or base64 conversion", e)
            throw IOException("Failed to zip files or convert to base64", e)
        } catch (e: Exception) {
            logger.error("Unexpected error during file processing", e)
            throw RuntimeException("Unexpected error in ZipFiles", e)
        } finally {
            // Ensure the temporary zip file is deleted even in case of errors
            if (zipFile.exists() && !zipFile.delete()) {
                logger.warn("Failed to clean up temporary zip file: ${zipFile.name}")
            }
        }
    }
}
