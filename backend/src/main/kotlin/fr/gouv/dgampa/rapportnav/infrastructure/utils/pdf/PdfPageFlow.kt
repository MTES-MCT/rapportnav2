package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import java.io.Closeable

/**
 * Lays content out top-down over as many A4 pages as needed: [ensureSpace] moves to a new page
 * when the next block would not fit above the bottom [margin], so that blocks are never cut.
 * A block taller than a page is split between its parts.
 */
class PdfPageFlow(private val pdf: PDDocument, private val margin: Float) : Closeable {

    lateinit var content: PDPageContentStream
        private set
    lateinit var page: PDPage
        private set

    /** y of the upper edge of the next block. */
    var top: Float = 0f

    val width: Float get() = page.mediaBox.width
    val contentWidth: Float get() = width - 2 * margin

    init {
        newPage()
    }

    fun newPage() {
        if (this::content.isInitialized) content.close()
        page = PDPage(PDRectangle.A4).also { pdf.addPage(it) }
        content = PDPageContentStream(pdf, page)
        top = pageTop
    }

    /**
     * Moves to a new page when a block of [height] does not fit in the rest of the page.
     * A block taller than a whole page stays where it is: a new page would not help, only leave a blank one.
     */
    fun ensureSpace(height: Float) {
        if (top - height < margin && top < pageTop) newPage()
    }

    /**
     * Draws [block] at [top], on a new page when it does not fit in the rest of this one.
     * A block taller than a page has its parts drawn one by one instead, each on a new page when needed.
     */
    fun draw(block: PdfBlock) {
        if (block.height > pageHeight && block.parts.isNotEmpty()) {
            block.parts.forEach { draw(it) }
            return
        }
        ensureSpace(block.height)
        block.draw(content, top)
        top -= block.height
    }

    /**
     * Keeps a group of blocks of [height] together: moves to a new page when the group does not fit in the rest
     * of the page but fits on a page. A taller group is left to the [ensureSpace] of each of its blocks.
     */
    fun keepTogether(height: Float) {
        if (height <= pageHeight) ensureSpace(height)
    }

    /** True when nothing has been laid out on the current page yet. */
    val isAtPageTop: Boolean get() = top >= pageTop

    private val pageTop: Float get() = page.mediaBox.height - margin

    /** Height available for content on a whole page. */
    private val pageHeight: Float get() = pageTop - margin

    override fun close() {
        content.close()
    }
}
