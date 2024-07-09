package fr.gouv.dgampa.rapportnav.infrastructure.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ExportExcelFile(private val filePath: String) {

    private var workbook: Workbook = XSSFWorkbook()
    private lateinit var file: FileInputStream

    init {
        FileInputStream(filePath).use { fis: FileInputStream ->
            this.workbook = XSSFWorkbook(fis)
            this.file= fis
        }
    }

    fun writeToCell(sheetName: String, rowIndex: Int, cellIndex: Int, value: String) {
        var sheet = workbook.getSheet(sheetName)
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName)
        }

        var row = sheet.getRow(rowIndex)
        if (row == null) {
            row = sheet.createRow(rowIndex)
        }

        var cell = row.getCell(cellIndex)
        if (cell == null) {
            cell = row.createCell(cellIndex)
        }

        cell.setCellValue(value)
    }

    @Throws(IOException::class)
    fun save(): ByteArray {
        FileOutputStream(filePath).use { fos: FileOutputStream ->
            workbook.write(fos)
            return this.file.readBytes()
        }
    }


}
