package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfFonts
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.horizontalLine
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.rect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.wrapText
import org.apache.pdfbox.pdmodel.font.PDFont

/**
 * A table: a header row in small bold text without border nor background, then the rows,
 * the first column on a grey background, each row underlined and spaced from the next. Cells wrap their text; columns share
 * the width according to [columnWeights] (equally by default).
 * Made of a part per row, the header staying with the first one, so that a table taller than a page is split between its rows.
 */
fun PdfContext.table(
    header: List<String>,
    rows: List<List<String>>,
    columnWeights: List<Float>? = null,
    fontSize: Float = 9f,
): PdfBlock {
    val layout = TableLayout(header, rows, fonts, width, columnWeights, fontSize)
    val lineHeight = fontSize * LINE_HEIGHT_RATIO

    val headerBlock = PdfBlock(layout.headerHeight) { top ->
        layout.headerLines.forEachIndexed { column, lines ->
            lines.forEachIndexed { index, line ->
                val baseline = top - CELL_PADDING - layout.headerFontSize - index * layout.headerFontSize * LINE_HEIGHT_RATIO
                text(line, x + layout.columnX(column) + CELL_PADDING, baseline, fonts.bold, layout.headerFontSize, PdfColors.TEXT_TITLE_GREY)
            }
        }
    }

    val rowBlocks = layout.rowLines.mapIndexed { row, cells ->
        val gap = if (row > 0) ROW_GAP else 0f
        val rowHeight = layout.rowHeights[row]
        PdfBlock(gap + rowHeight) { top ->
            val rowTop = top - gap
            rect(x, rowTop - rowHeight, layout.columnWidths[0], rowHeight, PdfColors.BACKGROUND_CONTRAST_GREY)
            cells.forEachIndexed { column, lines ->
                lines.forEachIndexed { index, line ->
                    val baseline = rowTop - CELL_PADDING - fontSize - index * lineHeight
                    text(line, x + layout.columnX(column) + CELL_PADDING, baseline, fonts.regular, fontSize)
                }
            }
            horizontalLine(x, rowTop - rowHeight, width, PdfColors.BORDER_DEFAULT_GREY, lineWidth = 0.5f)
        }
    }

    return PdfBlock.stack(listOf(PdfBlock.stack(listOf(headerBlock) + rowBlocks.take(1))) + rowBlocks.drop(1))
}

/** Column widths and wrapped cell lines of a table, shared by drawing and measuring. */
private class TableLayout(
    header: List<String>,
    rows: List<List<String>>,
    fonts: PdfFonts,
    width: Float,
    columnWeights: List<Float>?,
    fontSize: Float,
) {
    val headerFontSize = fontSize - 1f
    private val weights = columnWeights ?: List(header.size) { 1f }
    val columnWidths = weights.map { width * it / weights.sum() }

    val headerLines = header.mapIndexed { column, cell -> wrap(cell, fonts.bold, headerFontSize, column) }
    val rowLines = rows.map { cells -> cells.mapIndexed { column, cell -> wrap(cell, fonts.regular, fontSize, column) } }

    val headerHeight = cellsHeight(headerLines, headerFontSize)
    val rowHeights = rowLines.map { cellsHeight(it, fontSize) }

    fun columnX(column: Int): Float = columnWidths.take(column).sum()

    private fun wrap(text: String, font: PDFont, fontSize: Float, column: Int): List<String> =
        wrapText(text, font, fontSize, columnWidths[column] - 2 * CELL_PADDING).ifEmpty { listOf("") }

    private fun cellsHeight(cells: List<List<String>>, fontSize: Float): Float {
        val lineCount = cells.maxOf { it.size }
        return 2 * CELL_PADDING + fontSize + (lineCount - 1) * fontSize * LINE_HEIGHT_RATIO
    }
}

private const val CELL_PADDING = 5f
private const val LINE_HEIGHT_RATIO = 1.4f
private const val ROW_GAP = 4f
