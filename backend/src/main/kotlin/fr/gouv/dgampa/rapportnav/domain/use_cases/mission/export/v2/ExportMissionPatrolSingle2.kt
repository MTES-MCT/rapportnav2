package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.toMapForExport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.ComputePatrolData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils
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

@UseCase
class ExportMissionPatrolSingle2(
    private val formatActionsForTimeline2: FormatActionsForTimeline2,
    private val formatDateTime: FormatDateTime,
    private val getServiceById: GetServiceById,
    private val getComputeEnvMission: GetComputeEnvMission,
    private val computePatrolData: ComputePatrolData,

    @param:Value("\${rapportnav.rapport-patrouille.template.path}") private val docTemplatePath: String,
    @param:Value("\${rapportnav.rapport-patrouille.tmp_docx.path}") private val docTmpDOCXPath: String,
    @param:Value("\${rapportnav.rapport-patrouille.tmp_odt.path}") private val docTmpODTPath: String,
) {

    private val logger = LoggerFactory.getLogger(ExportMissionPatrolSingle2::class.java)


    /**
     * Returns a Rapport de Patrouille for one single mission
     * There will be one file for this mission
     *
     * @param missionId a Mission Ids
     * @return a MissionExportEntity with file name and content
     */
    fun execute(missionId: Int): MissionExportEntity? {

        val mission = getComputeEnvMission.execute(missionId = missionId)

        return createFile(mission)
    }

    fun createFile(mission: MissionEntity2?): MissionExportEntity? {
        if (mission == null) return null
        try {

            val patrolData = computePatrolData.execute(missionId = mission.id!!)
            val allActions = mission.actions

            val generalInfo: MissionGeneralInfoEntity2? = patrolData?.generalInfos
            val service = getServiceById.execute(generalInfo?.services?.first()?.id)
            val missionCrew: List<MissionCrewEntity>? = patrolData?.generalInfos?.crew

            val activity = patrolData?.activity

            val missionDuration = ComputeDurationUtils.durationInHours(patrolData?.startDateTimeUtc, patrolData?.endDateTimeUtc)

            val timeline = formatActionsForTimeline2.formatTimeline(allActions)

            val rescueInfo = patrolData?.otherActionsSummary?.get("rescue")?.toMapForExport()
            val nauticalEventsInfo = patrolData?.otherActionsSummary?.get("nauticalEvents")?.toMapForExport()
            val antiPollutionInfo = patrolData?.otherActionsSummary?.get("antiPollution")?.toMapForExport()
            val baaemAndVigimerInfo = patrolData?.otherActionsSummary?.get("baaemAndVigimer")?.toMapForExport()
            val illegalImmigrationInfo = patrolData?.otherActionsSummary?.get("illegalImmigration")?.toMapForExport()
            val envSurveillanceInfo = patrolData?.otherActionsSummary?.get("envSurveillance")?.toMapForExport()

            val crew: List<List<String?>> = listOf(
                listOf("Fonction", "Nom", "Observation (formation, repos, mission, stage...)")
            ) + missionCrew.orEmpty().map {
                listOf(
                    it.role?.title,
                    "${it.agent.firstName} ${it.agent.lastName}",
                    it.comment.takeIf { comment -> !comment.isNullOrEmpty() } ?: "Présent"
                )
            }


            // Bilan opérationnel
            val operationalSummary = patrolData?.operationalSummary
            val proFishingSeaSummary = operationalSummary?.proFishingSeaSummary
            val proFishingLandSummary = operationalSummary?.proFishingLandSummary
            val proSailingSeaSummary = operationalSummary?.proSailingSeaSummary
            val proSailingLandSummary = operationalSummary?.proSailingLandSummary
            val leisureSailingSeaSummary = operationalSummary?.leisureSailingSeaSummary
            val leisureSailingLandSummary = operationalSummary?.leisureSailingLandSummary
            val leisureFishingSummary = operationalSummary?.leisureFishingSummary
            val envSummary = operationalSummary?.envSummary

            val placeholders: Map<String, String?> = mapOf(
                "\${service}" to (service?.name ?: ""),
                "\${numRapport}" to formatDateTime.formatDate(mission.data?.startDateTimeUtc),
                "\${startDate}" to formatDateTime.formatDate(mission.data?.startDateTimeUtc),
                "\${endDate}" to formatDateTime.formatDate(mission.data?.endDateTimeUtc),
                "\${destinataireCopies}" to "",

                "\${dureeMission}" to missionDuration.toString(),
                "\${nbJoursMer}" to (activity?.get("atSea")?.get("nbOfDaysAtSea")?.toString() ?: ""),

                "\${totalPresenceMer}" to (activity?.get("atSea")?.get("totalDurationInHours")?.toString() ?: ""),
                "\${navEff}" to (activity?.get("atSea")?.get("navigationDurationInHours")?.toString()
                    ?: ""),
                "\${mouillage}" to (activity?.get("atSea")?.get("anchoredDurationInHours")?.toString() ?: ""),

                "\${totalPresenceQuai}" to (activity?.get("docked")?.get("totalDurationInHours")?.toString() ?: ""),
                "\${maintenance}" to (activity?.get("docked")?.get("maintenanceDurationInHours")?.toString() ?: ""),
                "\${meteo}" to (activity?.get("docked")?.get("meteoDurationInHours")?.toString() ?: ""),
                "\${representation}" to (activity?.get("docked")?.get("representationDurationInHours")?.toString() ?: ""),
                "\${admin}" to (activity?.get("docked")?.get("adminFormationDurationInHours")?.toString() ?: ""),
                "\${autre}" to (activity?.get("docked")?.get("otherDurationInHours")?.toString() ?: ""),
                "\${contrPort}" to (activity?.get("docked")?.get("contrPolDurationInHours")?.toString() ?: ""),
                "\${mco}" to (activity?.get("docked")?.get("mcoDurationInHours")?.toString() ?: ""),

                "\${totalIndisponibilite}" to (activity?.get("unavailable")?.get("totalDurationInHours")?.toString() ?: ""),
                "\${technique}" to (activity?.get("unavailable")?.get("technicalDurationInHours")?.toString() ?: ""),
                "\${personnel}" to (activity?.get("unavailable")?.get("personnelDurationInHours")?.toString() ?: ""),

                "\${patrouilleSurveillanceEnvInHours}" to (envSurveillanceInfo?.get("durationInHours")?.toFloatOrNull()
                    ?.toString() ?: ""),
                "\${patrouilleMigrantInHours}" to (illegalImmigrationInfo?.get("durationInHours")?.toFloatOrNull()
                    ?.toString() ?: ""),
                "\${rescueInfoCount}" to (rescueInfo?.get("count") ?: ""),
                "\${rescueInfoHours}" to (rescueInfo?.get("durationInHours") ?: ""),
                "\${nauticalEventsInfoCount}" to (nauticalEventsInfo?.get("count") ?: ""),
                "\${nauticalEventsInfoHours}" to (nauticalEventsInfo?.get("durationInHours") ?: ""),
                "\${antiPollutionInfoCount}" to (antiPollutionInfo?.get("count") ?: ""),
                "\${antiPollutionInfoHours}" to (antiPollutionInfo?.get("durationInHours") ?: ""),
                "\${baaemAndVigimerInfoCount}" to (baaemAndVigimerInfo?.get("count") ?: ""),
                "\${baaemAndVigimerInfoHours}" to (baaemAndVigimerInfo?.get("durationInHours") ?: ""),
                "\${baaemAndVigimerInfoShips}" to "",

                "\${distance}" to (generalInfo?.data?.distanceInNauticalMiles?.toString() ?: ""),
                "\${goMarine}" to (generalInfo?.data?.consumedGOInLiters?.toString() ?: ""),
                "\${essence}" to (generalInfo?.data?.consumedFuelInLiters?.toString() ?: ""),

                "\${operatingCosts}" to (generalInfo?.data?.operatingCostsInEuro?.toString() ?: ""),
                "\${fuelCosts}" to (generalInfo?.data?.fuelCostsInEuro?.toString() ?: ""),

                "\${observations}" to (mission.data?.observationsByUnit ?: ""),

                "\${nbInterns}" to (patrolData?.internTrainingSummary?.get("nbInterns").toString()),
                "\${nbAffMarInterns}" to (patrolData?.internTrainingSummary?.get("nbAffMarInterns").toString()),
                "\${nbPostgraduateInterns}" to (patrolData?.internTrainingSummary?.get("nbPostgraduateInterns").toString()),
                "\${nbHighSchoolInterns}" to (patrolData?.internTrainingSummary?.get("nbHighSchoolInterns").toString()),
                "\${nbOtherInterns}" to (patrolData?.internTrainingSummary?.get("nbOtherInterns").toString()),
                "\${totalInternDurationInDays}" to ((patrolData?.internTrainingSummary?.get("totalInternDurationInHours")
                    ?.div(24))?.or(0).toString()),
            )

            fun castLinkedHashMapToList(map: LinkedHashMap<String, Map<String, Int?>>?): List<List<String?>> {
                return map.orEmpty().map { (key, value) ->
                    // Create a list starting with the key, followed by the values from the inner map
                    listOf(key) + value.values.map { it?.toString() ?: "" }
                }
            }


            val inputStream = javaClass.getResourceAsStream(docTemplatePath)
                ?: throw IllegalArgumentException("Template file not found: $docTemplatePath")
            val document = XWPFDocument(inputStream)


            var paragraphs = document.paragraphs.toList()  // Create a copy of the paragraphs list
            for (paragraph in paragraphs) {
                replacePlaceholdersInParagraph(paragraph, placeholders)

                if (paragraph.text.contains("\${table_equipage}")) {
                    insertStyledTableAtParagraph(paragraph, crew)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
             for (paragraph in paragraphs) {
                replacePlaceholdersInParagraph(paragraph, placeholders)

                if (paragraph.text.contains("\${internPassengersTable")) {
                    insertStyledTableAtParagraph(paragraph, crew)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${proFishingSeaSummary}")) {
                    val header: List<String?> = listOf(
                        "",
                        "Nbre Navires contrôlés",
                        "Nbre contrôles pêche sanitaire",
                        "Nbre PV pêche sanitaire",
                        "Nbre d'infractions sans PV",
                        "Nbre PV équipmt sécu. permis nav.",
                        "Nbre PV titre navig. rôle/déc. eff",
                        "Nbre PV police navig.",
                        "Nbre navires déroutés"
                    )

                    val dataRows: List<List<String?>> = castLinkedHashMapToList(proFishingSeaSummary)
                    val table: List<List<String?>> = listOf(header) + dataRows
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${proFishingLandSummary}")) {
                    val header: List<String?> = listOf(
                        "",
                        "Nbre Navires contrôlés",
                        "Nbre contrôles pêche sanitaire",
                        "Nbre PV pêche sanitaire",
                        "Nbre d'infractions sans PV",
                        "Nbre PV titre navig. rôle/déc. eff",
                        "Nbre PV équipmt sécu. permis nav.",
                    )

                    val dataRows: List<List<String?>> = castLinkedHashMapToList(proFishingLandSummary)
                    val table: List<List<String?>> = listOf(header) + dataRows
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${proSailingSeaSummary}")) {
                    val header: List<String?> = listOf(
                        "Nbre Navires contrôlés",
                        "Nbre PV équipmt sécu. permis nav.",
                        "Nbre PV titre navig. rôle/déc. eff",
                        "Nbre PV police navig.",
                    )
                    val secondRow: List<String?> = proSailingSeaSummary?.values.orEmpty().map { value ->
                        if (value > 0) value.toString() else ""
                    }
                    val table: List<List<String?>> = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${proSailingLandSummary}")) {
                    val header: List<String?> = listOf(
                        "Nbre Navires contrôlés",
                        "Nbre PV titre navig. rôle/déc. eff",
                        "Nbre PV équipmt sécu. permis nav.",
                    )
                    val secondRow: List<String?> = proSailingLandSummary?.values.orEmpty().map { value ->
                        if (value > 0) value.toString() else ""
                    }
                    val table: List<List<String?>> = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }
            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${leisureSailingSeaSummary}")) {
                    val header: List<String?> = listOf(
                        "Nbre Navires contrôlés",
                        "Nbre PV équipmt sécu.",
                        "Nbre PV titre conduite",
                        "Nbre PV police navig.",
                    )

                    val secondRow: List<String?> = leisureSailingSeaSummary?.values.orEmpty().map { value ->
                        if (value > 0) value.toString() else ""
                    }
                    val table: List<List<String?>> = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }

            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${leisureSailingLandSummary}")) {
                    val header: List<String?> = listOf(
                        "Nbre Navires contrôlés",
                        "Nbre PV équipmt sécu.",
                        "Nbre PV titre conduite",
                    )
                    val secondRow: List<String?> = leisureSailingLandSummary?.values.orEmpty().map { value ->
                        if (value > 0) value.toString() else ""
                    }
                    val table: List<List<String?>> = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }


            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${envSummary}")) {
                    val header = listOf(
                        "Nbre surveillance Env",
                        "Nbre ctrl Env",
                        "Nbre de PV Env",
                    )

                    // Pick only the three needed values from your new summary map
                    val selectedKeys = listOf("nbSurveillances", "nbControls", "nbInfractionsWithRecord")

                    val secondRow = selectedKeys.map { key ->
                        val value = envSummary?.get(key)
                        when (value) {
                            is Number -> if (value.toInt() > 0) value.toString() else ""
                            else -> ""
                        }
                    }

                    val table = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }


            paragraphs = document.paragraphs.toList()
            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${leisureFishingSummary}")) {

                    val header: List<String?> = listOf(
                        "Nbre Navires contrôlés",
                        "Nbre de PV pêche de loisir",
                    )
                    val secondRow: List<String?> = leisureFishingSummary?.values.orEmpty().map { value ->
                        if (value > 0) value.toString() else ""
                    }
                    val table: List<List<String?>> = listOf(header, secondRow)
                    insertOperationalSummaryTableAtParagraph(paragraph, table)
                    break
                }
            }



            for (table in document.tables) {
                replacePlaceholdersInTable(table, placeholders)
            }

            for (paragraph in paragraphs) {
                if (paragraph.text.contains("\${timeline}")) {
                    // Insert the dynamic table where the placeholder is found
                    insertTimelineAtParagraph(paragraph, timeline)
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
                fileName = "rapport-patrouille_${service?.name ?: ""}_${formatDateTime.formatDate(mission.data?.startDateTimeUtc)}_${mission.id}.odt",
                fileContent = base64Content
            )

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
        if (table.rows.isNotEmpty()) {
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

    private fun insertOperationalSummaryTableAtParagraph(paragraph: XWPFParagraph, tableData: List<List<String?>>) {
        val document = paragraph.document as XWPFDocument

        // Create a new table directly at the placeholder position
        val table = document.insertNewTbl(paragraph.ctp.newCursor())

        setTableBorders(table)
        table.setWidth("100%")

        // Remove the initial empty row that is automatically created
        if (table.rows.isNotEmpty()) {
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
//        setColumnWidths(table, listOf(25, 25, 50))

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
