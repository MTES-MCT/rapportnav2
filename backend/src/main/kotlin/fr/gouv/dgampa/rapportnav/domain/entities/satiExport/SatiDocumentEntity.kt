package fr.gouv.dgampa.rapportnav.domain.entities.satiExport

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.ContactEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiPartyEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiVesselEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Data rendered in the SATI pdf of a fish control action.
 */
data class SatiDocumentEntity(
    val module: SatiModuleType,
    val vesselName: String?,
    val serviceName: String?,
    val inspectorFirstName: String?,
    val inspectorLastName: String?,
    /** Authority that appointed the principal inspector, null when not answered. */
    val inspectorAuthorityType: AuthorityType? = null,
    val startDatetimeUtc: Instant?,
    val endDatetimeUtc: Instant?,
    val latitude: Double?,
    val longitude: Double?,
    /** Where a control without coordinates took place, e.g. a land control. */
    val portName: String? = null,
    val portLocode: String? = null,
    val vessel: SatiVesselEntity?,
    /** Answer to "L'unité est-elle montée à bord ?" in MonitorFish, null when not answered. */
    val isUnitBoarded: Boolean? = null,
    val compliance: SatiVesselComplianceEntity = SatiVesselComplianceEntity(),
    val gearInspection: SatiGearInspectionEntity = SatiGearInspectionEntity(),
    val speciesInspection: SatiSpeciesInspectionEntity = SatiSpeciesInspectionEntity(),
    val conclusion: SatiConclusionEntity = SatiConclusionEntity(),
) {
    val owner: ContactEntity?
        get() = vessel?.owner?.contact

    /** The master's contact holds the owner's details when the owner is the master. */
    val master: ContactEntity?
        get() = vessel?.master?.contact

    /** Null when the vessel has no operator (charterer), as in the app: no identity filled in. */
    val operator: ContactEntity?
        get() = vessel?.operator.filledContact()

    /** Null when the vessel has no agent, as in the app: no identity filled in. */
    val agent: ContactEntity?
        get() = vessel?.agent.filledContact()

    private fun SatiPartyEntity?.filledContact(): ContactEntity? =
        this?.contact?.takeUnless { it.fullName.isNullOrBlank() }

    val inspectorName: String?
        get() = listOfNotNull(inspectorFirstName, inspectorLastName).joinToString(" ").ifBlank { null }

    /** Day of the control, in French time. */
    val controlDate: LocalDate?
        get() = startDatetimeUtc?.atZone(ZONE_ID)?.toLocalDate()

    /**
     * Inspection report reference, prefixed by the SATI module, e.g. "M1_ULAM_56_FLEUR_DE_LYS_2026-12-31".
     */
    val reference: String
        get() = listOf(module.name, serviceName, vesselName, controlDate?.toString())
            .joinToString("_") { it?.trim()?.uppercase()?.replace(WHITESPACES, "_") ?: MISSING_VALUE }

    companion object {
        private const val MISSING_VALUE = "INCONNU"
        private val WHITESPACES = Regex("\\s+")
        private val ZONE_ID = ZoneId.of("Europe/Paris")
    }
}
