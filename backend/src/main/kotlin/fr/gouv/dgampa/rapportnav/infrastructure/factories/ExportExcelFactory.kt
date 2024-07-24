package fr.gouv.dgampa.rapportnav.infrastructure.factories

import fr.gouv.dgampa.rapportnav.infrastructure.excel.ExportExcelFile

object ExportExcelFactory {

    fun create(filePath: String): ExportExcelFile {
        return ExportExcelFile(filePath)
    }
}
