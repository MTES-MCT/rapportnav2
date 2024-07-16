package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.infrastructure.excel.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.factories.ExportExcelFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@UseCase
class ExportMissionAEM(
    private val getMissionById: GetMissionById,
    private val fillAEMExcelRow: FillAEMExcelRow,
    @Value("\${aem.template.path}") private val aemTemplatePath: String,
    @Value("\${aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    @Value("\${aem.tmp_ods.path}") private val aemTmpODSPath: String,

) {

    private val logger: Logger = LoggerFactory.getLogger(ExportMissionAEM::class.java)
    fun execute(missionId: Int): MissionAEMExportEntity? {
        val path = Path.of(aemTemplatePath)
        val tmpPath = Path.of(aemTmpXLSXPath)
        Files.copy(path, tmpPath, StandardCopyOption.REPLACE_EXISTING)
        val excelFile: ExportExcelFile = ExportExcelFactory.create(tmpPath.toString())
        val mission: MissionEntity? = getMissionById.execute(missionId)


        if (mission !== null && mission.actions !== null) {
            val tableExport = AEMTableExport.fromMissionAction(mission.actions!!)
            fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", 3)
            excelFile.save()
            val odsFile = excelFile.convertToOds(tmpPath.toString(), aemTmpODSPath)
            val base64Content = excelFile.convertToBase64(odsFile)


            return MissionAEMExportEntity(
                fileName = "Rapport_AEM.ods",
                fileContent = base64Content
            )

        }

        logger.error("Can't load data for AEM export. Cause : Mission or actions are empty. Mission ID : $missionId")
        return null
    }


}
