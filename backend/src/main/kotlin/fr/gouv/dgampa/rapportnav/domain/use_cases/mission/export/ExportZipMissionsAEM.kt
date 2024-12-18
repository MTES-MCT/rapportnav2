package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionAEMExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FillAEMExcelRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.FileUtils
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.ExportExcelFile
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.OfficeConverter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@UseCase
class ExportZipMissionsAEM(
    private val getMissionById: GetMission,
    @Value("\${rapportnav.aem.template.path}") private val aemTemplatePath: String,
    @Value("\${rapportnav.aem.tmp_xlsx.path}") private val aemTmpXLSXPath: String,
    private val fillAEMExcelRow: FillAEMExcelRow
) {

    private val logger: Logger = LoggerFactory.getLogger(ExportZipMissionsAEM::class.java)

    fun execute(missionIds: List<Int>): MissionAEMExportEntity {
        var missions = mutableListOf<MissionEntity>()

        for (missionId in missionIds) {
            val mission = getMissionById.execute(missionId)
            if (mission != null) {
                missions.add(mission)
            }
        }

        if (missions.size > 0) {

            val inputStream = javaClass.getResourceAsStream(aemTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $aemTemplatePath")

            val tmpPath = Path.of(aemTmpXLSXPath)
            Files.copy(inputStream, tmpPath, StandardCopyOption.REPLACE_EXISTING)
            inputStream.close()

            val excelFile = ExportExcelFile(tmpPath.toString())

            val filesToZip = mutableListOf<File>();

            for (mission in missions) {
                val tableExport = AEMTableExport.fromMission(mission)
                fillAEMExcelRow.fill(tableExport, excelFile, "Synthese", 3)
                excelFile.save()

                logger.info("Excel file processed and saved")

                val odsFilePath = OfficeConverter().convert(tmpPath.toString(), "Mission-${mission.id}.ods")
                filesToZip.add(File(odsFilePath))
            }

            val zipFile = File("tmp_output.zip")

            val fileUtils = FileUtils();

            val outputZipFile = fileUtils.zip(zipFile, filesToZip)

            val base64Content = Base64Converter().convertToBase64(outputZipFile.absolutePath)

            for (file in filesToZip) {
                val isFileDeleted = file.delete() // remove file from project
                logger.info("${file.name} deletion : $isFileDeleted")

            }

            val isZipFileDeleted = outputZipFile.delete() // remove zip from project

            if (!isZipFileDeleted) {
                logger.info("output zip file not deleted")
            }

            return MissionAEMExportEntity(
                fileName = "tableaux_aem.zip",
                fileContent = base64Content
            )

        }
        throw RuntimeException("Error : Mission list is empty")
    }
}
