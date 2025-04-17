package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
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
class ExportMissionAEM(
    private val getMissionById: GetMission,
    private val fillAEMExcelRow: FillAEMExcelRow,
    @Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @Value("\${rapportnav.aem.tmp_ods.path}") private val aemTmpODSPath: String,

    ) {

    private val logger: Logger = LoggerFactory.getLogger(ExportMissionAEM::class.java)
    fun execute(missionId: String): MissionExportEntity? {
        return try {

            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            logger.info("Template file copied to temporary path: $tmpPath")

            val excelFile: ExportExcelFile = ExportExcelFile(tmpPath.toString())
            val mission: MissionEntity? = getMissionById.execute(missionId)

            if (mission?.actions != null) {
                val tableExport = AEMTableExport.fromMission(mission)
                fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", 3)
                excelFile.save()

                logger.info("Excel file processed and saved")

                val odsFile = OfficeConverter().convert(tmpPath.toString(), aemTmpODSPath)
                val base64Content = Base64Converter().convertToBase64(odsFile)

                logger.info("ODS file created and converted to Base64")

                return MissionExportEntity(
                    fileName = "Rapport_AEM.ods",
                    fileContent = base64Content
                )
            } else {
                logger.error("Mission or mission actions are null for missionId: $missionId")
                null
            }
        } catch (e: Exception) {
            logger.error("An error occurred during mission processing", e)
            null
        }
    }


}
