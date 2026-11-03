package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.PdfTag
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.checkRow
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.columns
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.labelledText
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.tag
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.MISSING_VALUE
import org.apache.pdfbox.pdmodel.font.PDFont

/*
 * Blocks shared by the sections of the SATI pdf.
 */

/** Labelled values shown side by side, in columns of equal width. */
internal typealias LabelledLine = List<Pair<String, String?>>

/** A block per line of labelled values in bold, or a "Non applicable" tag when [lines] is null. */
internal fun PdfContext.labelledLines(lines: List<LabelledLine>?): List<PdfBlock> =
    lines?.map { line ->
        columns(line) { (label, value) -> labelledValue(label, value ?: MISSING_VALUE, fonts.bold) }
    } ?: listOf(infoTag("Non applicable"))

/** "[label] : [value]", the label in regular grey. */
internal fun PdfContext.labelledValue(label: String, value: String, valueFont: PDFont): PdfBlock =
    labelledText("$label :", value, labelFont = fonts.regular, labelColor = PdfColors.TEXT_MENTION_GREY, valueFont = valueFont)

/** A blue tag telling there is nothing to show, e.g. "Non applicable". */
internal fun PdfContext.infoTag(label: String): PdfBlock =
    tag(PdfTag(label, backgroundColor = PdfColors.BACKGROUND_ACTION_LOW_BLUE_FRANCE, color = PdfColors.BLUE_FRANCE))

/** A check: its label on the left, its answer on the right (see [answer]). */
internal fun PdfContext.controlCheckRow(label: String, check: ControlCheck?): PdfBlock {
    val (answer, answerFont) = answer(check)
    return checkRow(label, answer, answerFont)
}

/** Text and font of an answer: "NON" in bold, "OUI" regular, "N/A" in italic. */
internal fun PdfContext.answer(check: ControlCheck?): Pair<String, PDFont> = when (check) {
    ControlCheck.YES -> "OUI" to fonts.regular
    ControlCheck.NO -> "NON" to fonts.bold
    ControlCheck.NOT_APPLICABLE -> "N/A" to fonts.italic
    null -> MISSING_VALUE to fonts.regular
}
