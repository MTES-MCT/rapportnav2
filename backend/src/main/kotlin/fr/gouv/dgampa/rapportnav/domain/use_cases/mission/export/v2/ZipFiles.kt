package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import org.slf4j.LoggerFactory
import java.io.File

@UseCase
class ZipFiles() {

    private val logger = LoggerFactory.getLogger(ZipFiles::class.java)

    /**
     * Zip files together and return is base64 representation
     *
     * @param files a list of files
     * @return The base64 encoded representation of a zip
     */
    fun execute(files: List<File>): String {
        val zipFile = File("tmp_output.zip")

        val fileUtils = FileUtils();

        val outputZipFile = fileUtils.zip(zipFile, files)

        val base64Content = Base64Converter().convertToBase64(outputZipFile.absolutePath)

        for (file in files) {
            val isFileDeleted = file.delete() // remove file from project
            logger.info("${file.name} deletion : $isFileDeleted")

        }

        val isZipFileDeleted = outputZipFile.delete() // remove zip from project

        if (!isZipFileDeleted) {
            logger.info("output zip file not deleted")
        }

        return base64Content
    }
}
