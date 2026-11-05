package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiDocumentEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.subsection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.LabelledLine
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatMeasure
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.labelledLines

internal fun PdfContext.generalInformationSection(document: SatiDocumentEntity): PdfSection {
    val vessel = document.vessel
    return PdfSection(
        "Informations générales",
        PdfSection.groupsWithLead(
            lead = emptyList(),
            blocks = listOf(
                subsection(
                    "Informations sur le navire contrôlé",
                    labelledLines(
                        listOf(
                            listOf(
                                "Nom du navire" to document.vesselName,
                                "CFR" to vessel?.immat,
                            ),
                            listOf(
                                "Marq. ext." to vessel?.extRef,
                                "N° d'appel radio" to vessel?.ircs,
                                "IMO" to vessel?.imo,
                            ),
                            listOf(
                                "Longueur" to formatMeasure(vessel?.length, "m"),
                                "Type" to vessel?.type,
                            ),
                            listOf("N° de marée" to vessel?.jpe?.tripNumber),
                        ),
                    ),
                ),
                subsection("Informations sur le propriétaire", labelledLines(contactLines(document.owner))),
                subsection("Informations sur le capitaine", labelledLines(contactLines(document.master))),
                subsection("Informations sur l'affréteur", labelledLines(document.operator?.let { contactLines(it) })),
                subsection("Informations sur l'agent", labelledLines(document.agent?.let { contactLines(it) })),
            ),
        ),
    )
}

/**
 * Lines of a contact. The app only asks for a full name ("Identité"): it stands for the last name
 * when first and last names are not known separately.
 */
private fun contactLines(contact: ContactEntity?): List<LabelledLine> = listOf(
    listOf(
        "Nom" to (contact?.lastName ?: contact?.fullName),
        "Prénom" to contact?.firstName,
        "Nationalité" to contact?.nationality,
    ),
    listOf(
        "Tél." to contact?.phone,
        "Email" to contact?.email,
    ),
    listOf("Adresse" to contact?.address?.fullAddress),
)
