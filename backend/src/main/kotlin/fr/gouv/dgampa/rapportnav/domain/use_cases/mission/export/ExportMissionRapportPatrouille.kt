package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.toMapForExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.status.GetNbOfDaysAtSeaFromNavigationStatus
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.EncodeSpecialChars
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.infrastructure.utils.Base64Converter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.office.OfficeConverter
import org.apache.poi.xwpf.usermodel.*
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import java.io.FileOutputStream
import java.math.BigInteger
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.io.path.Path
import kotlin.time.DurationUnit

@UseCase
class ExportMissionRapportPatrouille(
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getMission: GetMission,
    private val navActionStatus: INavActionStatusRepository,
    private val mapStatusDurations: MapStatusDurations,
    private val formatActionsForTimeline: FormatActionsForTimeline,
    private val getNbOfDaysAtSeaFromNavigationStatus: GetNbOfDaysAtSeaFromNavigationStatus,
    private val computeDurations: ComputeDurations,
    private val getInfoAboutNavAction: GetInfoAboutNavAction,
    private val encodeSpecialChars: EncodeSpecialChars,
    private val formatDateTime: FormatDateTime,
    private val getServiceById: GetServiceById,

    @Value("\${rapportnav.rapport-patrouille.template.path}") private val docTemplatePath: String,
    @Value("\${rapportnav.rapport-patrouille.tmp_docx.path}") private val docTmpDOCXPath: String,
    @Value("\${rapportnav.rapport-patrouille.tmp_odt.path}") private val docTmpODTPath: String,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionRapportPatrouille::class.java)

    fun exportOdt(missionId: Int): MissionExportEntity? {
        try {
            val mission: MissionEntity? = getMission.execute(missionId = missionId)
            if (mission == null) {
                logger.error("[RapportDePatrouille] - Mission not found for missionId: $missionId")
                return null
            }

            val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
            val service = getServiceById.execute(generalInfo?.serviceId)


            val agentsCrew: List<MissionCrewEntity> =
                agentsCrewByMissionId.execute(missionId = missionId, commentDefaultsToString = true)
            val statuses = navActionStatus.findAllByMissionId(missionId = missionId).sortedBy { it.startDateTimeUtc }
                .map { it.toActionStatusEntity() }

            val durations = mapStatusDurations.execute(mission, statuses, DurationUnit.HOURS)
            val missionDuration = computeDurations.convertFromSeconds(
                computeDurations.durationInSeconds(
                    mission.startDateTimeUtc,
                    mission.endDateTimeUtc,

                    ) ?: 0,
                DurationUnit.HOURS
            )

            val nbOfDaysAtSea = getNbOfDaysAtSeaFromNavigationStatus.execute(
                missionStartDateTime = mission.startDateTimeUtc,
                missionEndDateTime = mission.endDateTimeUtc,
                actions = statuses,
                durationUnit = DurationUnit.HOURS
            )

            val timeline = formatActionsForTimeline.formatTimeline(mission.actions)

            val rescueInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.RESCUE),
                actionSource = MissionSourceEnum.RAPPORTNAV,
            )?.toMapForExport()
            val nauticalEventsInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.NAUTICAL_EVENT),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val antiPollutionInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.ANTI_POLLUTION),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val baaemAndVigimerInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.VIGIMER, ActionType.BAAEM_PERMANENCE),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val illegalImmigrationInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.ILLEGAL_IMMIGRATION),
                actionSource = MissionSourceEnum.RAPPORTNAV
            )?.toMapForExport()
            val envSurveillanceInfo = getInfoAboutNavAction.execute(
                actions = mission.actions,
                actionTypes = listOf(ActionType.SURVEILLANCE),
                actionSource = MissionSourceEnum.MONITORENV
            )?.toMapForExport()

            val crew: List<List<String?>> = listOf(
                listOf("Fonction", "Nom", "Observation (formation, repos, mission, stage...)")
            ) + agentsCrew.map {
                listOf(
                    it.role.title,
                    "${it.agent.firstName} ${it.agent.lastName}",
                    it.comment.takeIf { comment -> !comment.isNullOrEmpty() } ?: "Présent"
                )
            }

            val placeholders: Map<String, String?> = mapOf(
                "\${service}" to (service?.name ?: ""),
                "\${numRapport}" to formatDateTime.formatDate(mission.startDateTimeUtc),
                "\${startDate}" to formatDateTime.formatDate(mission.startDateTimeUtc),
                "\${endDate}" to formatDateTime.formatDate(mission.endDateTimeUtc),
                "\${destinataireCopies}" to "CROSS Étel CNSP/CACEM",
                "\${observations}" to (mission.observationsByUnit?.let { encodeSpecialChars.escapeForXML(it) as String }
                    ?: ""),
                "\${dureeMission}" to missionDuration.toString(),
                "\${nbJoursMer}" to nbOfDaysAtSea.toString(),

                "\${totalPresenceMer}" to (durations["atSeaDurations"]?.get("total")?.toString() ?: ""),
                "\${navEff}" to (durations["atSeaDurations"]?.get("navigationEffective")?.toString()
                    ?: ""),
                "\${mouillage}" to (durations["atSeaDurations"]?.get("mouillage")?.toString() ?: ""),

                "\${totalPresenceQuai}" to (durations["dockingDurations"]?.get("total")?.toString() ?: ""),
                "\${maintenance}" to (durations["dockingDurations"]?.get("maintenance")?.toString() ?: ""),
                "\${meteo}" to (durations["dockingDurations"]?.get("meteo")?.toString() ?: ""),
                "\${representation}" to (durations["dockingDurations"]?.get("representation")?.toString() ?: ""),
                "\${admin}" to (durations["dockingDurations"]?.get("adminFormation")?.toString() ?: ""),
                "\${autre}" to (durations["dockingDurations"]?.get("autre")?.toString() ?: ""),
                "\${contrPort}" to (durations["dockingDurations"]?.get("contrPol")?.toString() ?: ""),

                "\${totalIndisponibilite}" to (durations["unavailabilityDurations"]?.get("total")?.toString() ?: ""),
                "\${technique}" to (durations["unavailabilityDurations"]?.get("technique")?.toString() ?: ""),
                "\${personnel}" to (durations["unavailabilityDurations"]?.get("personnel")?.toString() ?: ""),

                "\${patrouilleSurveillanceEnvInHours}" to (envSurveillanceInfo?.get("durationInHours")?.toFloatOrNull()
                    ?.toString() ?: ""),
                "\${patrouilleMigrantInHours}" to (illegalImmigrationInfo?.get("durationInHours")?.toFloatOrNull()
                    ?.toString() ?: ""),
                "\${rescueInfoCount}" to (rescueInfo?.get("count") ?: ""),
                "\${rescueInfoHours}" to (rescueInfo?.get("durationInHours") ?: ""),
                "\${nauticalEventsInfoCount}" to (nauticalEventsInfo?.get("count") ?: ""),
                "\${nauticalEventsInfoHours}" to (nauticalEventsInfo?.get("durationInHours") ?: ""),
                "\${antiPollutionInfoCount}" to (nauticalEventsInfo?.get("count") ?: ""),
                "\${antiPollutionInfoHours}" to (nauticalEventsInfo?.get("durationInHours") ?: ""),
                "\${baaemAndVigimerInfoCount}" to (baaemAndVigimerInfo?.get("count") ?: ""),
                "\${baaemAndVigimerInfoHours}" to (baaemAndVigimerInfo?.get("durationInHours") ?: ""),
                "\${baaemAndVigimerInfoShips}" to "",
                "\${distance}" to (generalInfo?.distanceInNauticalMiles?.toString() ?: ""),
                "\${goMarine}" to (generalInfo?.consumedGOInLiters?.toString() ?: ""),
                "\${essence}" to (generalInfo?.consumedFuelInLiters?.toString() ?: "")
            )


            val inputStream = javaClass.getResourceAsStream(docTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $docTemplatePath")
            val document = XWPFDocument(inputStream)


            val paragraphs = document.paragraphs.toList()  // Create a copy of the paragraphs list
            for (paragraph in paragraphs) {
                replacePlaceholdersInParagraph(paragraph, placeholders)
                if (paragraph.text.contains("\${table_equipage}")) {
                    // Insert the dynamic table where the placeholder is found
                    insertStyledTableAtParagraph(paragraph, crew)
                }
            }

            for (table in document.tables) {
                replacePlaceholdersInTable(table, placeholders)
            }

            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${timeline}")) {
                    // Insert the dynamic table where the placeholder is found
                    insertTimelineAtParagraph(paragraph, timeline)
                    val a = 0
                    break
                }
            }

            setFontForAllParagraphs(document, "Arial")

            // Save the updated document to a new file
            FileOutputStream(docTmpDOCXPath).use { outputStream ->
                document.write(outputStream)
            }

            // Close resources
            document.close()
            inputStream.close()

            println("Template filled successfully and saved as $docTmpDOCXPath")

            val odtFile = OfficeConverter().convert(Path(docTmpDOCXPath).toString(), Path(docTmpODTPath).toString())
            val base64Content = Base64Converter().convertToBase64(odtFile)


            return MissionExportEntity(
                fileName = "Rapport de patrouille ${service?.name ?: ""} ${formatDateTime.formatDate(mission.startDateTimeUtc)}.odt",
                fileContent = base64Content
            )

//            return exportRepository.exportOdt(exportParams)
        } catch (e: Exception) {
            logger.error("[RapportDePatrouille] - Error building data before sending it to RapportNav1 : ${e.message}")
            return null
        }
    }

    private fun setFontForAllParagraphs(document: XWPFDocument, fontFamily: String) {
        for (paragraph in document.paragraphs) {
            val runs = paragraph.runs
            for (run in runs) {
                run.fontFamily = fontFamily
            }
        }
    }

    private fun replacePlaceholdersInParagraph(paragraph: XWPFParagraph, placeholders: Map<String, String?>) {
        // Loop through the runs (pieces of text) in the paragraph
        for (run in paragraph.runs) {
            var text = run.text() // Get the text in the run
            if (!text.isNullOrEmpty()) {
                // Replace each placeholder with the corresponding value
                for ((placeholder, value) in placeholders) {
                    if (text.contains(placeholder)) {
                        text = text.replace(placeholder, value ?: "")
                    }
                }
                // Set the updated text back to the run
                run.setText(text, 0)
            }
        }
    }

    private fun replacePlaceholdersInTable(table: XWPFTable, placeholders: Map<String, String?>) {
        for (row in table.rows) {
            for (cell in row.tableCells) {
                // Loop through each cell in the table
                for (paragraph in cell.paragraphs) {
                    // Replace placeholders in the paragraph of the cell
                    replacePlaceholdersInParagraph(paragraph, placeholders)
                }
            }
        }
    }

    private fun insertTimelineAtParagraph(paragraph: XWPFParagraph, timeline: Map<LocalDate, List<String>>?) {
        val document = paragraph.document as XWPFDocument
        val formatter = DateTimeFormatter.ofPattern("EEEE dd MMMM yyyy", Locale.FRENCH)

        // Get the position of the old paragraph
        val oldIndex = document.getPosOfParagraph(paragraph)

        // Create a new paragraph at the position of the old one
        val newParagraph = document.insertNewParagraph(paragraph.ctp.newCursor())
        newParagraph.style = paragraph.style

        if (!timeline.isNullOrEmpty()) {
            timeline.keys.sorted().forEach { date ->
                val dateRun = newParagraph.createRun()
                dateRun.isBold = true
                dateRun.fontSize = 11
                dateRun.setText(date.format(formatter))
                dateRun.addBreak()

                timeline[date]?.forEach { event ->
                    val eventRun = newParagraph.createRun()
                    eventRun.fontSize = 11
                    eventRun.setText("• $event")
                    eventRun.addBreak()
                }

                newParagraph.createRun().addBreak() // Add an extra line break between date entries
            }
        } else {
            val run = newParagraph.createRun()
            run.setText("")
        }

        // Remove the old paragraph
        document.removeBodyElement(oldIndex + 1)
    }


    private fun insertStyledTableAtParagraph(paragraph: XWPFParagraph, tableData: List<List<String?>>) {
        val document = paragraph.document as XWPFDocument

        // Create a new table directly at the placeholder position
        val table = document.insertNewTbl(paragraph.ctp.newCursor())

        setTableBorders(table)
        table.setWidth("100%")

        // Remove the initial empty row that is automatically created
        if (table.rows.size > 0) {
            table.removeRow(0)
        }

        // Create the header row
        val headerRow = table.createRow()
        for (headerText in tableData[0]) {
            val cell = headerRow.addNewTableCell()
            cell.setText(headerText ?: "")
            styleCell(cell, bold = true, fontSize = 10, alignment = ParagraphAlignment.LEFT)
        }
        headerRow.height = 400
        setColumnWidths(table, listOf(25, 25, 50))

        // Insert the rest of the rows (data)
        for (rowData in tableData.drop(1)) {
            val row = table.createRow()
            row.height = 400

            for ((i, cellData) in rowData.withIndex()) {
                val cell = row.getCell(i) ?: row.addNewTableCell()
                cell.setText(cellData ?: "")
                styleCell(cell, bold = false, fontSize = 10, alignment = ParagraphAlignment.LEFT)
            }

            // Ensure each row has the same number of columns as the header
            val headerColumnCount = tableData[0].size
            while (row.tableCells.size < headerColumnCount) {
                row.addNewTableCell()
            }
        }

        // Remove the placeholder paragraph
        val position = document.getPosOfParagraph(paragraph)
        document.removeBodyElement(position)
    }


    // Style an individual table cell
    private fun styleCell(cell: XWPFTableCell, bold: Boolean, fontSize: Int, alignment: ParagraphAlignment) {
        // Ensure the cell has at least one paragraph
        val paragraph = if (cell.paragraphs.isEmpty()) {
            cell.addParagraph()
        } else {
            cell.paragraphs[0]
        }

        // Create a new run for the text inside the paragraph
        val run = if (paragraph.runs.isEmpty()) {
            paragraph.createRun()
        } else {
            paragraph.runs[0]  // Get the existing run if it exists
        }

        // Set the text styling (bold, font size)
        run.isBold = bold
        run.fontSize = fontSize

        // Align the paragraph
        paragraph.alignment = alignment
    }


    // Set specific column widths
    private fun setColumnWidths(table: XWPFTable, percentages: List<Int>) {
        val tableWidth = 10000  // Total table width in twips (100% width = 10000 twips)

        // Set the table width to 100% (10000 twips)
        val tblPr = table.ctTbl.tblPr ?: table.ctTbl.addNewTblPr()
        val tblW = tblPr.addNewTblW()
        tblW.w = BigInteger.valueOf(tableWidth.toLong())
        tblW.type = STTblWidth.PCT  // Set width as percentage

        // Apply column widths to all rows
        for (row in table.rows) {
            for ((j, widthPercent) in percentages.withIndex()) {
                if (j < row.tableCells.size) {
                    val cell = row.getCell(j)
                    val tcPr = cell.ctTc.addNewTcPr()
                    val cellWidth = tcPr.addNewTcW()
                    cellWidth.w = BigInteger.valueOf((tableWidth * widthPercent / 100).toLong())
                    cellWidth.type = STTblWidth.DXA  // Set width in twips (DXA)
                }
            }
        }
    }


    fun setTableBorders(table: XWPFTable) {
        val borderType = STBorder.SINGLE
        val borderSize = BigInteger.valueOf(4) // Border width in 1/8th points (4 = 0.5 points)
        val borderColor = "000000" // Black color in hex

        val tableBorders = table.ctTbl.tblPr.tblBorders

        tableBorders.top.setVal(borderType)
        tableBorders.top.setSz(borderSize)
        tableBorders.top.setColor(borderColor)

        tableBorders.bottom.setVal(borderType)
        tableBorders.bottom.setSz(borderSize)
        tableBorders.bottom.setColor(borderColor)

        tableBorders.left.setVal(borderType)
        tableBorders.left.setSz(borderSize)
        tableBorders.left.setColor(borderColor)

        tableBorders.right.setVal(borderType)
        tableBorders.right.setSz(borderSize)
        tableBorders.right.setColor(borderColor)

        tableBorders.insideH.setVal(borderType)
        tableBorders.insideH.setSz(borderSize)
        tableBorders.insideH.setColor(borderColor)

        tableBorders.insideV.setVal(borderType)
        tableBorders.insideV.setSz(borderSize)
        tableBorders.insideV.setColor(borderColor)
    }


}