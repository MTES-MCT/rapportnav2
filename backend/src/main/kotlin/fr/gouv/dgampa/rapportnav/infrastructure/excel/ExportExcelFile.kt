package fr.gouv.dgampa.rapportnav.infrastructure.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class ExportExcelFile(private val filePath: String) {

    private var workbook: Workbook = XSSFWorkbook()

    init {
        FileInputStream(filePath).use { fis: FileInputStream ->
            this.workbook = XSSFWorkbook(fis)
        }
    }

    fun writeToCell(sheetName: String, cellValue: String, value: Int) {
        var sheet = workbook.getSheet(sheetName)
        if (sheet == null) {
            sheet = workbook.createSheet(sheetName)
        }

        val cellRef = cellReference(cellValue)

        var row = cellRef["row"]?.let { sheet.getRow(it) }
        if (row == null) {
            row = cellRef["row"]?.let { sheet.getRow(it) }
        }

        var cell = cellRef["cell"]?.let { row?.getCell(it) }
        if (cell == null) {
            cell = cellRef["cell"]?.let { row?.createCell(it) }
        }

        cell?.setCellValue(value.toDouble())
    }

    fun cellReference(cellRef: String): Map<String, Int>
    {
        val cellReference = CellReference(cellRef)
        return mapOf("cell" to cellReference.col.toInt(), "row" to cellReference.row)
    }


    @Throws(IOException::class)
    fun save() {
        FileOutputStream(filePath).use { fos: FileOutputStream ->
            workbook.write(fos)
        }
    }


}
