package fr.gouv.dgampa.rapportnav.infrastructure.utils

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileUtils(
) {

    fun zip(outputZipFile: File, files: List<File>): File {
        ZipOutputStream(FileOutputStream(outputZipFile)).use { zipOut ->
            files.forEach { file ->
                FileInputStream(file).use { fis ->
                    val zipEntry = ZipEntry(file.name)
                    zipOut.putNextEntry(zipEntry)
                    fis.copyTo(zipOut)
                    zipOut.closeEntry()
                }
            }
        }
        return outputZipFile
    }
}
