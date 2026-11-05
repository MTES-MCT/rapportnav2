package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.rect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import java.awt.Color

/** A section title in white on a blue France band spanning the width. */
fun PdfContext.sectionTitle(
    title: String,
    fontSize: Float = 14f,
    height: Float = 32f,
    paddingX: Float = 12f,
) = PdfBlock(height) { top ->
    rect(x, top - height, width, height, PdfColors.BLUE_FRANCE)
    text(title, x + paddingX, top - height / 2 - fontSize * 0.35f, fonts.bold, fontSize, Color.WHITE)
}
