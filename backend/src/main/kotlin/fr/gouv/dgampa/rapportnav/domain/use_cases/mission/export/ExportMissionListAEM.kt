package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.OfficeConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@UseCase
class ExportMissionListAEM(
    @Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @Value("\${rapportnav.aem.tmp_ods.path}") private val aemTmpODSPath: String,
    private val fillAEMExcelRow: FillAEMExcelRow,

    ) {
    private val logger: Logger = LoggerFactory.getLogger(ExportMissionListAEM::class.java)

    fun execute(missions: List<MissionEntity>): MissionAEMExportEntity?
    {
        return try {
            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            logger.info("Template file copied to temporary path: $tmpPath")

            val excelFile = ExportExcelFile(tmpPath.toString())
            val tableExportList = AEMTableExport.fromMissionList(missions)

            if (tableExportList.isNotEmpty()) {
                var rowStart = 3

                for (tableExport in tableExportList) {
                    fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", rowStart)
                    rowStart++
                }
                excelFile.save()

                logger.info("Excel file processed and saved for export on missions list")

                val odsFile = OfficeConverter().convert(tmpPath.toString(), aemTmpODSPath)
                val base64Content = Base64Converter().convertToBase64(odsFile)

                return MissionAEMExportEntity(
                    fileName = "Rapport_AEM.ods",
                    fileContent = base64Content
                )
            }

            logger.error("Actions in missions list are null")
            null
        } catch (e: Exception) {
            logger.error("An error occurred during mission processing", e)
            null
        }
    }
}
