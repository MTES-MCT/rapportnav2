package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.PDPageContentStream

/**
 * Content laid out top-down by [PdfPageFlow]: its [height] is known before it is drawn, so that it can be
 * moved to the next page rather than cut. [draw] takes the y of the block's upper edge.
 * A block made of [parts] (drawn one below the other, together as [draw]) is split between them when taller than a page.
 */
class PdfBlock(
    val height: Float,
    val parts: List<PdfBlock> = emptyList(),
    val draw: PDPageContentStream.(top: Float) -> Unit,
) {
    companion object {
        /** [blocks] drawn one below the other, as a single block split between them when taller than a page. */
        fun stack(blocks: List<PdfBlock>) = PdfBlock(blocks.sumOfFloat { it.height }, parts = blocks) { top ->
            var blockTop = top
            blocks.forEach { block ->
                block.draw(this, blockTop)
                blockTop -= block.height
            }
        }

        /** Empty space of [height]. */
        fun space(height: Float) = PdfBlock(height) {}
    }
}

// sums in Double, as the standard library has no sumOf for Float
internal inline fun <T> Iterable<T>.sumOfFloat(selector: (T) -> Float): Float = sumOf { selector(it).toDouble() }.toFloat()
