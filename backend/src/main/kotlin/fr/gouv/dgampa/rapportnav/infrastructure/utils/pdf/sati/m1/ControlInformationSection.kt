package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.CoordinatesFormatter
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.subsection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.warningBox
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatUtc
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfLabels
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.label
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.labelledLines

internal fun PdfContext.controlInformationSection(document: SatiDocumentEntity) = PdfSection(
    "Informations sur le contrôle",
    PdfSection.groupsWithLead(
        // only when answered "Non" in MonitorFish: it is mostly not answered
        lead = listOfNotNull(
            if (document.isUnitBoarded == false) warningBox("L'unité de contrôle n'a pas pu monter à bord du navire contrôlé") else null,
        ),
        blocks = listOf(
            subsection(
                "Autorité d'inspection",
                labelledLines(
                    listOf(
                        listOf("Administration" to SatiPdfLabels.ADMINISTRATION),
                        listOf("Unité" to document.serviceName),
                        listOf("État membre" to SatiPdfLabels.MEMBER_STATE),
                    ),
                ),
            ),
            subsection(
                "Inspecteur responsable",
                labelledLines(
                    listOf(
                        listOf("Nom" to document.inspectorLastName),
                        listOf("Prénom" to document.inspectorFirstName),
                        listOf("Autorité investie du pouvoir de nomination" to document.inspectorAuthorityType?.label),
                    ),
                ),
            ),
            subsection(
                "Début et fin d'inspection",
                labelledLines(
                    listOf(
                        listOf("Heure de début" to formatUtc(document.startDatetimeUtc)),
                        listOf("Heure de fin" to formatUtc(document.endDatetimeUtc)),
                    ),
                ),
            ),
            subsection("Lieu d'inspection", labelledLines(listOf(listOf(location(document))))),
        ),
    ),
)

/** The coordinates of the control when there are some, otherwise its port, e.g. for a land control. */
private fun location(document: SatiDocumentEntity): Pair<String, String?> {
    val coordinates = document.latitude?.let { latitude ->
        document.longitude?.let { longitude -> CoordinatesFormatter.toDmd(latitude, longitude) }
    }
    val port = listOfNotNull(document.portName, document.portLocode?.let { "($it)" })
        .takeIf { it.isNotEmpty() }
        ?.joinToString(" ")
    return if (coordinates == null && port != null) "Port" to port else "Coordonnées GPS (DMD)" to coordinates
}
