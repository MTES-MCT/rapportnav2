package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf

import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfLayout.SPACING
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.sectionTitle

/** Blocks kept on a same page when they fit on one, [gap] apart. */
class PdfGroup(val blocks: List<PdfBlock>, val gap: Float = SPACING) {
    val height: Float get() = blocks.sumOfFloat { it.height } + gap * maxOf(blocks.size - 1, 0)
}

/**
 * A title band, kept on the page of the first group, then [groups] [SPACING] apart. A block is never cut;
 * a [keepWhole] section is moreover kept whole on a page when it fits on one.
 */
class PdfSection(val title: String, groups: List<PdfGroup>, val keepWhole: Boolean = false) {
    val groups = groups.filter { it.blocks.isNotEmpty() }

    companion object {
        /** Groups where [lead] stays with the first of [blocks], each other block standing alone. */
        fun groupsWithLead(lead: List<PdfBlock>, blocks: List<PdfBlock>): List<PdfGroup> =
            listOf(PdfGroup(lead + blocks.take(1))) + blocks.drop(1).map { PdfGroup(listOf(it)) }
    }
}

/** Lays [section] out from [PdfPageFlow.top], its title drawn within [context]. */
fun PdfPageFlow.render(section: PdfSection, context: PdfContext) {
    val groups = section.groups
    val title = context.sectionTitle(section.title)
    if (section.keepWhole) {
        keepTogether(sectionTitleHeight(title) + groups.sumOfFloat { it.height } + SPACING * maxOf(groups.size - 1, 0))
    }
    drawSectionTitle(title, firstGroupHeight = groups.firstOrNull()?.height ?: 0f)
    groups.forEachIndexed { groupIndex, group ->
        if (groupIndex > 0) {
            top -= SPACING
            keepTogether(group.height)
        }
        group.blocks.forEachIndexed { blockIndex, block ->
            if (blockIndex > 0) top -= group.gap
            draw(block)
        }
    }
    top -= SPACING
}

/** Draws a section [title], kept on the same page as the first group below it (of [firstGroupHeight]). */
private fun PdfPageFlow.drawSectionTitle(title: PdfBlock, firstGroupHeight: Float) {
    ensureSpace(sectionTitleHeight(title) + firstGroupHeight)
    if (!isAtPageTop) top -= SECTION_GAP
    title.draw(content, top)
    top -= title.height + SPACING
}

/** Height of a section [title], with the gap separating it from the previous section (none at the top of a page). */
private fun PdfPageFlow.sectionTitleHeight(title: PdfBlock): Float =
    (if (isAtPageTop) 0f else SECTION_GAP) + title.height + SPACING

// added to the spacing after the previous section
private const val SECTION_GAP = 24f
