package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.OfficeConverter
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.nio.file.Files
import java.nio.file.StandardCopyOption

@UseCase
class ExportMissionAEMSingle(
    private val getMission: GetMission,
    private val fillAEMExcelRow: FillAEMExcelRow,
    private val formatDateTime: FormatDateTime,
    @param:Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @param:Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @param:Value("\${rapportnav.aem.tmp_ods.path}") private val aemTmpODSPath: String,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionAEMSingle::class.java)

    fun execute(missionId: Int): MissionExportEntity? {
        val mission: MissionEntity? = getMission.execute(missionId = missionId)
        if (mission == null) {
            logger.error("[ExportAEM] - Mission not found for missionId: $missionId")
            return null
        }
        return createFile(mission)
    }

    fun createFile(mission: MissionEntity?): MissionExportEntity? {
        if (mission == null) return null
        return try {

            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = java.nio.file.Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            logger.info("Template file copied to temporary path: $tmpPath")

            val excelFile: ExportExcelFile = ExportExcelFile(tmpPath.toString())

            if (!mission.actions.isNullOrEmpty()) {
                val tableExport = AEMTableExport.fromMission(mission)
                fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", 3)
                excelFile.save()

                logger.info("Excel file processed and saved")

                val odsFile = OfficeConverter().convert(tmpPath.toString(), aemTmpODSPath)
                val base64Content = Base64Converter().convertToBase64(odsFile)

                logger.info("ODS file created and converted to Base64")

                return MissionExportEntity(
                    fileName = "tableau-AEM_${formatDateTime.formatDate(mission.startDateTimeUtc)}_${mission.id}.ods",
                    fileContent = base64Content
                )
            } else {
                logger.error("Mission or mission actions are null for missionId: ${mission.id}")
                null
            }
        } catch (e: Exception) {
            logger.error("An error occurred during mission processing", e)
            null
        }
    }

}
