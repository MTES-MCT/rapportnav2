package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMTableExport2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
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
class ExportMissionAEM2(
    @Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @Value("\${rapportnav.aem.tmp_ods.path}") private val aemTmpODSPath: String,
    private val fillAEMExcelRow: FillAEMExcelRow,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getEnvActionByMissionId: GetEnvActionListByMissionId,
    private val getNavActionByMissionId: GetNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetFishActionListByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
) {
    private val logger: Logger = LoggerFactory.getLogger(ExportMissionAEM2::class.java)

    fun execute(missionIds: List<Int>): MissionExportEntity? {

        val tableExportList = getAemTableExport(missionIds)

        return try {
            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            logger.info("Template file copied to temporary path: $tmpPath")

            val excelFile = ExportExcelFile(tmpPath.toString())
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

                return MissionExportEntity(
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

    private fun getAemTableExport(missionIds: List<Int>) = missionIds.map {
        val envMission = getEnvMissionById2.execute(it)
        val envActions = getEnvActionByMissionId.execute(it)
        val navActions = getNavActionByMissionId.execute(it)
        val fishActions = getFIshListActionByMissionId.execute(it)
        val generalInfo = getMissionGeneralInfoByMissionId.execute(it)
        AEMTableExport2.fromMissionAction(
            navActions = navActions,
            envActions = envActions,
            fishActions = fishActions,
            missionEndDateTimeUtc = envMission?.endDateTimeUtc,
            nbrOfRecognizedVessel = generalInfo?.nbrOfRecognizedVessel?.toDouble()
        )
    }
}
