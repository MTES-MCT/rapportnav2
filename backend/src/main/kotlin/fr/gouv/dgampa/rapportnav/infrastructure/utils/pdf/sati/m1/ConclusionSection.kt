package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiConclusionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiInfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfColors
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfGroup
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.PdfTag
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.borderedBox
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.paragraph
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.subsection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.tags
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.MISSING_VALUE
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.label
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.answer
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.labelledValue

/** The outcome as tags, a box per infraction, then the comments of the inspector and of the controlled person. */
internal fun PdfContext.conclusionSection(conclusion: SatiConclusionEntity): PdfSection {
    val comments = listOf(
        subsection("Commentaire de l'inspecteur", commentRows(conclusion.inspectorComment, conclusion.inspectorSigned)),
        subsection(
            "Commentaire de la personne contrôlée",
            commentRows(conclusion.controlledPersonComment, conclusion.controlledPersonSigned),
        ),
    )
    return PdfSection(
        "Conclusions de l'inspection",
        groups = PdfSection.groupsWithLead(listOf(tags(conclusionTags(conclusion))), conclusion.infractions.map { infractionBox(it) }) +
            comments.map { PdfGroup(listOf(it)) },
        keepWhole = true,
    )
}

/** Without infraction, only says so; with infractions, also tells whether gears and species were seized. */
private fun conclusionTags(conclusion: SatiConclusionEntity): List<PdfTag> {
    if (!conclusion.hasInfractions) return listOf(PdfTag("Sans infraction", PdfColors.SUCCESS))
    return listOfNotNull(
        PdfTag("Avec infraction", PdfColors.ERROR),
        if (conclusion.hasSomeGearsSeized) PdfTag("Appréhension engin", PdfColors.ERROR) else PdfTag("Sans appréhension", PdfColors.SUCCESS),
        if (conclusion.hasSomeSpeciesSeized) {
            val quantity = conclusion.speciesQuantitySeized?.let { " ($it kg)" }.orEmpty()
            PdfTag("Appréhension espèces$quantity", PdfColors.ERROR)
        } else null,
    )
}

/** A bordered box for an infraction: its name, tags for its outcome and its NATINF, then its observations. */
private fun PdfContext.infractionBox(infraction: SatiInfractionEntity): PdfBlock = borderedBox {
    val title = paragraph(
        // one description per NATINF
        "Infraction : ${infraction.descriptions.joinToString(" ; ").ifEmpty { MISSING_VALUE }}",
        font = fonts.bold,
        fontSize = INFRACTION_TITLE_FONT_SIZE,
        lineHeight = INFRACTION_TITLE_LINE_HEIGHT,
        color = PdfColors.TEXT_TITLE_GREY,
    )
    val outcome = tags(
        listOf(
            PdfTag(infraction.infractionType?.label ?: MISSING_VALUE),
            PdfTag(
                "${infractionCategory(infraction) ?: MISSING_VALUE} / NATINF : " +
                    infraction.natinfs.joinToString(", ").ifEmpty { MISSING_VALUE }
            ),
        )
    )
    val comments = infraction.comments?.takeIf { it.isNotBlank() }
        ?.let { listOf(PdfBlock.space(INFRACTION_GAP), paragraph(it)) }
        .orEmpty()
    listOf(title, PdfBlock.space(INFRACTION_GAP), outcome) + comments
}

/** The category of the NATINF of a MonitorFish infraction, or the kind of RapportNav control the infraction was found in. */
private fun infractionCategory(infraction: SatiInfractionEntity): String? =
    infraction.natinfCategory ?: infraction.controlType?.label

/** The comment in italic (or that there is none), and whether the report was signed, styled as a check. */
private fun PdfContext.commentRows(comment: String?, signed: Boolean?): List<PdfBlock> {
    val commentRow = comment?.takeIf { it.isNotBlank() }
        ?.let { labelledValue("Commentaire", it, fonts.italic) }
        ?: labelledValue("Commentaire", "Pas de commentaire", fonts.regular)
    val (answer, answerFont) = answer(signed?.let { if (it) ControlCheck.YES else ControlCheck.NO })
    return listOf(commentRow, labelledValue("Rapport signé", answer, answerFont))
}

private const val INFRACTION_TITLE_FONT_SIZE = 11f
private const val INFRACTION_TITLE_LINE_HEIGHT = 16f
private const val INFRACTION_GAP = 8f
