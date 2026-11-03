package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfGroup
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.table
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.MISSING_VALUE
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.codeAndName
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatWeight
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.label
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.infoTag

internal fun PdfContext.discardsSection(speciesInspection: SatiSpeciesInspectionEntity): PdfSection {
    val discards = speciesInspection.discards
    val content = if (discards.isEmpty()) {
        infoTag("Aucun rejet")
    } else {
        table(
            header = listOf("Espèce", "Qté rejetée", "Nature du rejet"),
            rows = discards.map { discard ->
                listOf(
                    codeAndName(discard.speciesCode, speciesInspection.speciesName(discard)),
                    formatWeight(discard.rejectedWeight),
                    // as in MonitorFish, e.g. "DIM - de minimis"
                    discard.discardReason?.let { "${it.name} - ${it.label}" } ?: MISSING_VALUE,
                )
            },
            columnWeights = listOf(2f, 1f, 1.5f),
        )
    }
    return PdfSection("Rejets", listOf(PdfGroup(listOf(content))))
}
