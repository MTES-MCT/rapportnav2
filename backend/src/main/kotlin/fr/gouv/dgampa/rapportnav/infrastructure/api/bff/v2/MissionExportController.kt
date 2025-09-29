package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportModeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.ExportReportTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionReports
import org.slf4j.LoggerFactory
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

/**
 * Controller responsible for exporting mission reports.
 *
 * This endpoint allows the generation of various types of documents for one or multiple missions, such as:
 * - Rapport de patrouille
 * - Tableaux AEM
 *
 * Depending on the provided parameters, the exported document(s) will be tailored to the selected report type
 * and export mode.
 */
@Controller
class MissionExportController(
    private val exportMissionReports: ExportMissionReports,
) {

    private val logger = LoggerFactory.getLogger(MissionExportController::class.java)

    @QueryMapping
    fun exportMissionReports(
        @Argument missionIds: List<Int>,
        @Argument exportMode: ExportModeEnum,
        @Argument reportType: ExportReportTypeEnum,
    ): MissionExportEntity? {
        try {
//            throw Exception("fdfdfd")
            val output: MissionExportEntity? =
                exportMissionReports.execute(missionIds = missionIds.distinct(), exportMode = exportMode, reportType = reportType)
            return output
        } catch (e: Exception) {
            logger.error("API exportMissionReports - error while generating documents", e)
            throw Exception(e)
//             uncomment following lines for mocking/dev
//            return MissionExportEntity(
//                fileName = "rapport-de-patrouille.odt",
//                fileContent = "UEsDBBQABgAIAAAAIQCzgd16AAAAAAAAAAAAAAAACwAJAG1pbWV0eXBlYXBwbGljYXRpb24vdm5kLm9hc2lzLm9wZW5kb2N1bWVu", // odt
//                fileName = "rapport-de-patrouille.zip",
//                fileContent = "UEsFBgAAAAAAAAAAAAAAAAAAAAAAAA==", // zip
//            )
        }
    }

}
