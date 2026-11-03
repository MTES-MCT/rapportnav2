package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.rect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.strokedRect
import java.awt.Color

/** [content] on a [background], with an optional left [accent] border (as DSFR's callout). Kept whole. */
fun PdfContext.filledBox(
    background: Color,
    accent: Color? = null,
    padding: Float = BOX_PADDING,
    content: PdfContext.() -> List<PdfBlock>,
): PdfBlock {
    val accentWidth = if (accent != null) ACCENT_WIDTH else 0f
    val body = PdfBlock.stack(inset(left = accentWidth + padding, right = padding).content())
    val height = body.height + 2 * padding
    return PdfBlock(height) { top ->
        rect(x, top - height, width, height, background)
        if (accent != null) rect(x, top - height, ACCENT_WIDTH, height, accent)
        body.draw(this, top - padding)
    }
}

/** [content] within a grey border. Kept whole. */
fun PdfContext.borderedBox(padding: Float = BOX_PADDING, content: PdfContext.() -> List<PdfBlock>): PdfBlock {
    val body = PdfBlock.stack(inset(padding).content())
    val height = body.height + 2 * padding
    return PdfBlock(height) { top ->
        strokedRect(x, top - height, width, height, PdfColors.BORDER_DEFAULT_GREY)
        body.draw(this, top - padding)
    }
}

private const val BOX_PADDING = 16f
private const val ACCENT_WIDTH = 4f
