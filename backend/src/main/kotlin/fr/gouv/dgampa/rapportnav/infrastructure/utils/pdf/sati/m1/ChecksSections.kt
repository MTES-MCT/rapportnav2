package fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.m1

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.ControlCheck
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiGearInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiSpeciesInspectionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.satiExport.SatiVesselComplianceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfBlock
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfContext
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfGroup
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.PdfSection
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.callout
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.components.table
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.MISSING_VALUE
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.codeAndName
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatMeasure
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.SatiPdfFormatter.formatWeight
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.label
import fr.gouv.dgampa.rapportnav.infrastructure.utils.pdf.sati.controlCheckRow

internal fun PdfContext.vesselComplianceSection(compliance: SatiVesselComplianceEntity) = checksSection(
    title = "Conformité du navire",
    checks = listOf(
        "Échelle de coupée présente et conforme" to compliance.gangwayPresentAndCompliant,
        "Bonne émission VMS" to compliance.emitsVms,
        "Bonne émission AIS" to compliance.emitsAis,
        "Journal de pêche ouvert avant le contrôle" to compliance.logbookOpenedPriorToControl,
        "Déclarations journal de pêche conformes à l'activité du navire" to compliance.logbookMatchesActivity,
        "Autorisations de pêche (AEP, ANP, licences locales) conformes à l'activité du navire" to
            compliance.licencesMatchActivity,
        "Licence de pêche européenne valide" to compliance.europeanFishingLicenceValid,
        "Plan d'arrimage présent et conforme" to compliance.stowagePlanPresent,
        "Autorisation pour la pesée à bord" to compliance.onboardWeighingPermit,
        "Certificat de pesée présent et systèmes de pesée à bord valides" to
            compliance.weighingCertificateAndSystemsValid,
    ),
    observationsTitle = "Observations sur les obligations déclaratives et autorisations de pêche",
    observations = listOfNotNull(compliance.licencesAndLogbookObservations),
)

internal fun PdfContext.gearInspectionSection(gearInspection: SatiGearInspectionEntity) = checksSection(
    title = "Inspection des engins",
    checks = listOf(
        "Engin contrôlé" to gearInspection.gearControlled,
        "Marquage de l'engin conforme" to gearInspection.gearMarkingCompliant,
    ),
    table = gearInspection.controlledGears.takeIf { it.isNotEmpty() }?.let { gears ->
        table(
            header = listOf("Engin", "Maillage déclaré", "Maillage mesuré", "Épaisseur moy. fil", "Type de fil"),
            rows = gears.map { gear ->
                listOf(
                    codeAndName(gear.gearCode, gear.gearName),
                    formatMeasure(gear.declaredMesh, "mm") ?: MISSING_VALUE,
                    if (gear.hasUncontrolledMesh) "Non mesuré" else formatMeasure(gear.controlledMesh, "mm") ?: MISSING_VALUE,
                    formatMeasure(gear.averageWireThickness, "mm") ?: MISSING_VALUE,
                    gear.wireType?.label ?: MISSING_VALUE,
                )
            },
            // the gear column holds the gear code and name
            columnWeights = listOf(2f, 1f, 1f, 1f, 1f),
        )
    },
    observationsTitle = "Observations sur les engins",
    // as in the app, each gear has its own observations ("autres mesures et dispositifs")
    observations = gearInspection.gears
        .filter { !it.comments.isNullOrBlank() }
        .map { gear -> "${gear.gearCode ?: MISSING_VALUE} : ${gear.comments}" },
)

internal fun PdfContext.speciesInspectionSection(speciesInspection: SatiSpeciesInspectionEntity) = checksSection(
    title = "Inspection des espèces",
    checks = listOf(
        "Poids des espèces vérifié" to speciesInspection.speciesWeightControlled,
        "Taille des espèces vérifiée" to speciesInspection.speciesSizeControlled,
        "Arrimage séparé des espèces soumises à plan" to speciesInspection.separateStowageOfPreservedSpecies,
        "Arrimage séparé des poissons n'ayant pas la taille requise" to speciesInspection.underSizedSeparateStowage,
        "Enregistrement séparé des poissons n'ayant pas la taille requise" to
            speciesInspection.underSizedSeparateRecording,
    ),
    table = speciesInspection.species.takeIf { it.isNotEmpty() }?.let { species ->
        table(
            header = listOf("Espèce", "Qté déclarée", "Qté estimée", "Qté sous-taille", "Présentation du poisson", "Zone"),
            rows = species.map {
                listOf(
                    codeAndName(it.speciesCode, it.speciesName),
                    formatWeight(it.declaredWeight),
                    formatWeight(it.controlledWeight),
                    formatWeight(it.underSizedWeight),
                    it.presentationCodes.orEmpty().joinToString(", ").ifEmpty { MISSING_VALUE },
                    it.faoZones.orEmpty().joinToString(", ").ifEmpty { MISSING_VALUE },
                )
            },
            // the species column holds the species code and name
            columnWeights = listOf(1.6f, 1f, 1f, 1f, 1.2f, 1f),
        )
    },
    observationsTitle = "Observations sur les espèces",
    observations = listOfNotNull(speciesInspection.speciesObservations),
)

/**
 * A section of checks (label on the left, answer on the right), then an optional [table]
 * and a callout of observations (not drawn when all are blank). Kept whole on a page when it fits on one,
 * otherwise split between its checks (with the title) and its table (with the observations).
 */
private fun PdfContext.checksSection(
    title: String,
    checks: List<Pair<String, ControlCheck?>>,
    table: PdfBlock? = null,
    observationsTitle: String,
    observations: List<String>,
): PdfSection {
    val paragraphs = observations.filter { it.isNotBlank() }
    return PdfSection(
        title,
        groups = listOf(
            PdfGroup(checks.map { (label, check) -> controlCheckRow(label, check) }, gap = 0f),
            PdfGroup(listOfNotNull(table, paragraphs.takeIf { it.isNotEmpty() }?.let { callout(observationsTitle, it) })),
        ),
        keepWhole = true,
    )
}
