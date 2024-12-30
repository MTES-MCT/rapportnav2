package fr.gouv.dgampa.rapportnav.infrastructure.utils

import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class FileUtils {

    /**
     * Compresses a list of files into a single ZIP archive.
     *
     * @param outputZipFile The output ZIP file.
     * @param files The list of files to be included in the ZIP archive.
     * @return The created ZIP file.
     * @throws IOException If any I/O operation fails.
     */
    @Throws(IOException::class)
    fun zip(outputZipFile: File, files: List<File>): File {
        if (files.isEmpty()) {
            throw IllegalArgumentException("File list cannot be empty.")
        }

        ZipOutputStream(FileOutputStream(outputZipFile)).use { zipOut ->
            files.forEach { file ->
                require(file.exists() && file.isFile) {
                    "File '${file.absolutePath}' does not exist or is not a valid file."
                }

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
