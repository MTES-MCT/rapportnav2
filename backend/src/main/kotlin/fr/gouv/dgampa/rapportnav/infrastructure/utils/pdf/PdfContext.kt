package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.common.PDRectangle

/**
 * Where components are laid out: with [fonts], in the horizontal band starting at [x] and spanning [width].
 * Components are built from a context, so that they need not be given their fonts and position.
 */
class PdfContext(
    val fonts: PdfFonts,
    val x: Float = PdfLayout.MARGIN,
    val width: Float = PdfLayout.CONTENT_WIDTH,
) {
    /** The band narrowed by [left] and [right]. */
    fun inset(left: Float, right: Float = left) = PdfContext(fonts, x + left, width - left - right)

    /** The band split into [count] columns of equal width, [gap] apart. */
    fun splitColumns(count: Int, gap: Float): List<PdfContext> {
        val columnWidth = (width - gap * (count - 1)) / count
        return List(count) { index -> PdfContext(fonts, x + index * (columnWidth + gap), columnWidth) }
    }
}

/** Layout tokens shared by the components, for A4 pages. */
object PdfLayout {
    const val MARGIN = 40f
    val CONTENT_WIDTH = PDRectangle.A4.width - 2 * MARGIN
    /** Between blocks. */
    const val SPACING = 16f
    const val BODY_FONT_SIZE = 10f
    const val BODY_LINE_HEIGHT = 15f
}
