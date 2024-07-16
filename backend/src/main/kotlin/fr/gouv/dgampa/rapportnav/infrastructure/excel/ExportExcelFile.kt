package fr.gouv.dgampa.rapportnav.infrastructure.excel

import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellReference
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

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

    private fun cellReference(cellRef: String): Map<String, Int>
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

    fun convertToOds(xlsxPath: String, odsPath: String, exportDir: String): String {
        val process = ProcessBuilder("soffice", "--headless", "--convert-to", "ods", "--outdir", exportDir, xlsxPath).start()
        process.waitFor()

        val odsFilePath = Path.of(exportDir, Path.of(xlsxPath).fileName.toString().replace(".xlsx", ".ods"))

        return odsFilePath.toString()
    }

    fun convertToBase64(filePath: String) : String {
        val odsByte = Files.readAllBytes(Path.of(filePath))
        Files.delete(Path.of(filePath))
        return Base64.getEncoder().encodeToString(odsByte)
    }


}
