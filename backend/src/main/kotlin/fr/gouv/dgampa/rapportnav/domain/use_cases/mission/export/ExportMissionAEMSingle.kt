package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMTableExport2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeEnvActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeFishActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
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
class ExportMissionAEMSingle(
    @param:Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @param:Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @param:Value("\${rapportnav.aem.tmp_ods.path}") private val aemTmpODSPath: String,
    private val fillAEMExcelRow: FillAEMExcelRow,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getEnvActionByMissionId: GetComputeEnvActionListByMissionId,
    private val getNavActionByMissionId: GetComputeNavActionListByMissionId,
    private val getFIshListActionByMissionId: GetComputeFishActionListByMissionId,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val getComputeEnvMission: GetComputeEnvMission,
) {
    private val logger: Logger = LoggerFactory.getLogger(ExportMissionAEMSingle::class.java)

    fun execute(missionId: Int): MissionExportEntity {
        val mission = getComputeEnvMission.execute(missionId = missionId)
            ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
                message = "Mission not found: $missionId"
            )

        return createFile(mission)!!
    }

    fun createFile(mission: MissionEntity2?): MissionExportEntity? {
        return try {
            val tableExport = getAemData(mission?.id)
            if (tableExport == null) return null

            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            logger.info("Template file copied to temporary path: $tmpPath")

            val excelFile = ExportExcelFile(tmpPath.toString())
            var rowStart = 3

            fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", rowStart)

            excelFile.save()

            logger.info("Excel file processed and saved for export on missions list")

            val odsFile = OfficeConverter().convert(tmpPath.toString(), aemTmpODSPath)
            val base64Content = Base64Converter().convertToBase64(odsFile)

            return MissionExportEntity(
                fileName = "Rapport_AEM.ods",
                fileContent = base64Content
            )

        } catch (e: BackendInternalException) {
            throw e  // Re-throw domain exceptions as-is
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to create AEM report for mission ${mission?.id}",
                originalException = e
            )
        }
    }

    fun getAemData(missionId: Int?): AEMTableExport2?  {
        if(missionId == null) return null
        val envMission = getEnvMissionById2.execute(missionId)
        val envActions = getEnvActionByMissionId.execute(missionId)
        val navActions = getNavActionByMissionId.execute(missionId)
        val fishActions = getFIshListActionByMissionId.execute(missionId)
        val generalInfo = getMissionGeneralInfoByMissionId.execute(missionId)
        return AEMTableExport2.fromMissionAction(
            navActions = navActions,
            envActions = envActions,
            fishActions = fishActions,
            missionEndDateTimeUtc = envMission?.endDateTimeUtc,
            nbrOfRecognizedVessel = generalInfo?.nbrOfRecognizedVessel?.toDouble()
        )
    }

}

