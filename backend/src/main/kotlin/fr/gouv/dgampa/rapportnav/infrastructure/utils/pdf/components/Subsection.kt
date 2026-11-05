package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.horizontalLine
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text

/** A title underlined across the width above [body], kept whole. */
fun PdfContext.subsection(title: String, body: List<PdfBlock>): PdfBlock =
    PdfBlock.stack(listOf(subsectionTitle(title)) + body)

private fun PdfContext.subsectionTitle(title: String, fontSize: Float = 12f) =
    PdfBlock(fontSize + UNDERLINE_OFFSET + GAP_BELOW) { top ->
        val baseline = top - fontSize
        text(title, x, baseline, fonts.bold, fontSize, PdfColors.TEXT_TITLE_GREY)
        horizontalLine(x, baseline - UNDERLINE_OFFSET, width, PdfColors.BORDER_DEFAULT_GREY, lineWidth = 1f)
    }

// below the baseline
private const val UNDERLINE_OFFSET = 5f
private const val GAP_BELOW = 8f
