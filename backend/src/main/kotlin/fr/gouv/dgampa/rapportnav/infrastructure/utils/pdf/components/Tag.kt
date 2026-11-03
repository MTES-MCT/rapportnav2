package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.circle
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.roundedRect
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.text
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.textWidth
import org.apache.pdfbox.pdmodel.PDPageContentStream
import java.awt.Color

/** A pill-shaped tag after monitor-ui's Tag (https://mtes-mct.github.io/monitor-ui/?path=/story/elements-tag--tag). */
class PdfTag(
    val label: String,
    /** Color of a bullet before the label, none when null. */
    val bulletColor: Color? = null,
    val backgroundColor: Color = PdfColors.BACKGROUND_CONTRAST_GREY,
    val color: Color = PdfColors.TEXT_TITLE_GREY,
)

/** [tags] side by side, going to the next line when needed. */
fun PdfContext.tags(tags: List<PdfTag>): PdfBlock {
    val positions = tagPositions(tags)
    val lineCount = (positions.maxOfOrNull { it.second } ?: 0) + 1
    return PdfBlock(lineCount * TAG_HEIGHT + (lineCount - 1) * TAG_GAP) { top ->
        positions.forEachIndexed { index, (tagX, line) ->
            drawTag(this, tags[index], x = x + tagX, top = top - line * (TAG_HEIGHT + TAG_GAP))
        }
    }
}

fun PdfContext.tag(tag: PdfTag): PdfBlock = tags(listOf(tag))

/** (x, line index) of each tag. */
private fun PdfContext.tagPositions(tags: List<PdfTag>): List<Pair<Float, Int>> {
    var x = 0f
    var line = 0
    return tags.map { tag ->
        val tagWidth = tagWidth(tag)
        if (x > 0f && x + tagWidth > width) {
            x = 0f
            line++
        }
        (x to line).also { x += tagWidth + TAG_GAP }
    }
}

private fun PdfContext.tagWidth(tag: PdfTag): Float =
    PADDING_X +
        (if (tag.bulletColor != null) BULLET_SIZE + ITEM_GAP else 0f) +
        textWidth(tag.label, fonts.regular, FONT_SIZE) +
        PADDING_X

private fun PdfContext.drawTag(content: PDPageContentStream, tag: PdfTag, x: Float, top: Float) = with(content) {
    val bottom = top - TAG_HEIGHT
    var contentX = x + PADDING_X

    setNonStrokingColor(tag.backgroundColor)
    roundedRect(x, bottom, tagWidth(tag), TAG_HEIGHT, radius = TAG_HEIGHT / 2)
    fill()

    if (tag.bulletColor != null) {
        setNonStrokingColor(tag.bulletColor)
        circle(contentX + BULLET_SIZE / 2, bottom + TAG_HEIGHT / 2, BULLET_SIZE / 2)
        fill()
        contentX += BULLET_SIZE + ITEM_GAP
    }

    text(tag.label, contentX, bottom + (TAG_HEIGHT - FONT_SIZE) / 2 + 2f, fonts.regular, FONT_SIZE, tag.color)
}

private const val TAG_HEIGHT = 16f
private const val TAG_GAP = 8f
private const val FONT_SIZE = 9.5f
private const val PADDING_X = 6f
private const val BULLET_SIZE = 7f
private const val ITEM_GAP = 4f
