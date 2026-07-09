package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.LocationType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule
import fr.gouv.dgampa.rapportnav.domain.validation.RequiredFieldsValidator.Rule.Companion.conditional
import java.time.Instant

/**
 * Registry of all validation policies, ordered newest-first.
 *
 * Each policy is an immutable, versioned set of required-field rules.
 * A mission's start date determines which policy applies via [forMissionStartDate].
 *
 * To add stricter rules for new missions, create a new policy that builds on the previous one.
 */
object ValidationPolicies {

    // =========================================================================
    // Shared constants
    // =========================================================================
    private const val MSG_START_DATE_REQUIRED = "La date de début est requise"
    private const val MSG_END_DATE_REQUIRED = "La date de fin est requise"

    // =========================================================================
    // MissionGeneralInfoEntity rules
    // =========================================================================
    private const val SERVICE_TYPE_PAM_DESC = "serviceType = PAM"
    private val IS_PAM: (MissionGeneralInfoEntity) -> Boolean = { it.service?.serviceType == ServiceTypeEnum.PAM }

    private val generalInfoRules: List<Rule<MissionGeneralInfoEntity>> = listOf(
        conditional("distanceInNauticalMiles", "La distance en milles nautiques est requise",
            SERVICE_TYPE_PAM_DESC, IS_PAM) { it.distanceInNauticalMiles },
        conditional("consumedGOInLiters", "La consommation de GO en litres est requise",
            SERVICE_TYPE_PAM_DESC, IS_PAM) { it.consumedGOInLiters },
        conditional("consumedFuelInLiters", "La consommation de carburant en litres est requise",
            SERVICE_TYPE_PAM_DESC, IS_PAM) { it.consumedFuelInLiters },
        conditional("nbrOfRecognizedVessel", "Le nombre de navires reconnus est requis",
            SERVICE_TYPE_PAM_DESC, IS_PAM) { it.nbrOfRecognizedVessel },

        conditional("interMinisterialServices", "Les services interministériels sont requis",
            "serviceType = ULAM et isWithInterMinisterialService = true",
            { it.service?.serviceType == ServiceTypeEnum.ULAM && it.isWithInterMinisterialService == true }) { it.interMinisterialServices?.ifEmpty { null } }
    )

    // =========================================================================
    // MissionNavActionEntity rules
    // =========================================================================
    private val ACTION_TYPES_REQUIRING_END_DATE = listOf(
        ANTI_POLLUTION, BAAEM_PERMANENCE, CONTROL, RESCUE,
        VIGIMER, REPRESENTATION, PUBLIC_ORDER, ILLEGAL_IMMIGRATION, NAUTICAL_EVENT,
        CONDUCT_HEARING, COMMUNICATION, TRAINING, UNIT_MANAGEMENT_PLANNING, UNIT_MANAGEMENT_TRAINING,
        CONTROL_SECTOR, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL,
        RESOURCES_MAINTENANCE, MEETING, PV_DRAFTING, HEARING_CONDUCT, LAND_SURVEILLANCE,
        FISHING_SURVEILLANCE, UNIT_MANAGEMENT_OTHER, OTHER, MARITIME_SURVEILLANCE
    )

    private val ACTION_TYPES_REQUIRING_LOCATION = listOf(
        RESCUE, ILLEGAL_IMMIGRATION, ANTI_POLLUTION
    )

    private val ACTION_TYPES_WITH_LOCATION_TYPE = listOf(
        CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL
    )

    private val navActionRules: List<Rule<MissionNavActionEntity>> = listOf(
        // Always required
        Rule.always("id", "L'identifiant est requis") { it.id },
        Rule.always("missionId", "L'identifiant de mission est requis") { it.missionId },
        Rule.always("actionType", "Le type d'action est requis") { it.actionType },
        Rule.always("startDateTimeUtc", MSG_START_DATE_REQUIRED) { it.startDateTimeUtc },

        // Required for specific actionTypes
        Rule.forActionTypes("endDateTimeUtc", ACTION_TYPES_REQUIRING_END_DATE, MSG_END_DATE_REQUIRED) { it.endDateTimeUtc },
        Rule.forActionTypes("latitude", ACTION_TYPES_REQUIRING_LOCATION, "La latitude est requise") { it.latitude },
        Rule.forActionTypes("longitude", ACTION_TYPES_REQUIRING_LOCATION, "La longitude est requise") { it.longitude },

        // Location-type-dependent rules
        conditional("latitude", "La latitude est requise",
            "actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = GPS",
            { it.actionType in ACTION_TYPES_WITH_LOCATION_TYPE && it.locationType == LocationType.GPS },
            relatedActionTypes = ACTION_TYPES_WITH_LOCATION_TYPE, extraCondition = "locationType = GPS") { it.latitude },
        conditional("longitude", "La longitude est requise",
            "actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = GPS",
            { it.actionType in ACTION_TYPES_WITH_LOCATION_TYPE && it.locationType == LocationType.GPS },
            relatedActionTypes = ACTION_TYPES_WITH_LOCATION_TYPE, extraCondition = "locationType = GPS") { it.longitude },
        conditional("city", "La commune est requise",
            "actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = COMMUNE",
            { it.actionType in ACTION_TYPES_WITH_LOCATION_TYPE && it.locationType == LocationType.COMMUNE },
            relatedActionTypes = ACTION_TYPES_WITH_LOCATION_TYPE, extraCondition = "locationType = COMMUNE") { it.city },
        conditional("zipCode", "Le code postal est requis",
            "actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = COMMUNE",
            { it.actionType in ACTION_TYPES_WITH_LOCATION_TYPE && it.locationType == LocationType.COMMUNE },
            relatedActionTypes = ACTION_TYPES_WITH_LOCATION_TYPE, extraCondition = "locationType = COMMUNE") { it.zipCode },
        conditional("portLocode", "Le port est requis",
            "actionType ∈ {CONTROL, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL} et locationType = PORT",
            { it.actionType in ACTION_TYPES_WITH_LOCATION_TYPE && it.locationType == LocationType.PORT },
            relatedActionTypes = ACTION_TYPES_WITH_LOCATION_TYPE, extraCondition = "locationType = PORT") { it.portLocode },

        // CONTROL-specific
        Rule.forActionTypes("controlMethod", listOf(CONTROL), "La méthode de contrôle est requise") { it.controlMethod },
        Rule.forActionTypes("vesselIdentifier", listOf(CONTROL), "L'identifiant du navire est requis") { it.vesselIdentifier },
        Rule.forActionTypes("vesselType", listOf(CONTROL), "Le type de navire est requis") { it.vesselType },
        Rule.forActionTypes("vesselSize", listOf(CONTROL), "La taille du navire est requise") { it.vesselSize },
        Rule.forActionTypes("identityControlledPerson", listOf(CONTROL), "L'identité de la personne contrôlée est requise") { it.identityControlledPerson },

        // ILLEGAL_IMMIGRATION-specific
        Rule.forActionTypes("nbOfInterceptedVessels", listOf(ILLEGAL_IMMIGRATION), "Le nombre de navires interceptés est requis") { it.nbOfInterceptedVessels },
        Rule.forActionTypes("nbOfInterceptedMigrants", listOf(ILLEGAL_IMMIGRATION), "Le nombre de migrants interceptés est requis") { it.nbOfInterceptedMigrants },
        Rule.forActionTypes("nbOfSuspectedSmugglers", listOf(ILLEGAL_IMMIGRATION), "Le nombre de passeurs suspectés est requis") { it.nbOfSuspectedSmugglers },

        // STATUS-specific
        Rule.forActionTypes("status", listOf(STATUS), "Le statut est requis") { it.status },
        conditional("reason", "La raison est requise",
            "actionType = STATUS et status ∈ {DOCKED, UNAVAILABLE}",
            { it.actionType == STATUS && (it.status == ActionStatusType.DOCKED || it.status == ActionStatusType.UNAVAILABLE) },
            relatedActionTypes = listOf(STATUS), extraCondition = "status ∈ {DOCKED, UNAVAILABLE}") { it.reason },

        // INQUIRY-specific
        Rule.forActionTypes("nbrOfHours", listOf(INQUIRY), "Le nombre d'heures est requis") { it.nbrOfHours },

        // TRAINING-specific
        Rule.forActionTypes("trainingType", listOf(TRAINING), "Le type de formation est requis") { it.trainingType },
        Rule.forActionTypes("unitManagementTrainingType", listOf(UNIT_MANAGEMENT_TRAINING), "Le type de formation est requis") { it.unitManagementTrainingType },

        // RESOURCES_MAINTENANCE-specific
        Rule.forActionTypes("resourceType", listOf(RESOURCES_MAINTENANCE), "Le type de ressource est requis") { it.resourceType },
        Rule.forActionTypes("resourceIds", listOf(RESOURCES_MAINTENANCE), "L'identifiant de ressource est requis") { it.resourceIds },

        // CONTROL_NAUTICAL_LEISURE-specific
        Rule.forActionTypes("nbrOfControl", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles est requis") { it.nbrOfControl },
        Rule.forActionTypes("nbrOfControlAmp", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles AMP est requis") { it.nbrOfControlAmp },
        Rule.forActionTypes("nbrOfControl300m", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles 300m est requis") { it.nbrOfControl300m },
        Rule.forActionTypes("leisureType", listOf(CONTROL_NAUTICAL_LEISURE), "Le type de loisir est requis") { it.leisureType },

        // CONTROL_SECTOR-specific
        Rule.forActionTypes("sectorType", listOf(CONTROL_SECTOR), "Le type de secteur est requis") { it.sectorType },
        Rule.forActionTypes("sectorEstablishmentType", listOf(CONTROL_SECTOR), "Le type d'établissement est requis") { it.sectorEstablishmentType },
        conditional("establishment", "L'établissement est requis",
            "actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType ∉ {FISH_AUCTION, LANDING_SITE}",
            {
                it.actionType == CONTROL_SECTOR
                && (
                    (it.sectorType == SectorType.FISHING && it.sectorEstablishmentType !in listOf(SectorEstablishmentType.FISH_AUCTION, SectorEstablishmentType.LANDING_SITE))
                    ||
                    (it.sectorType == SectorType.PLEASURE)
                )
            },
            relatedActionTypes = listOf(CONTROL_SECTOR), extraCondition = "sectorType = FISHING et sectorEstablishmentType ∉ {FISH_AUCTION, LANDING_SITE}") { it.establishment },
        conditional("fishAuction", "La criée est requise",
            "actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType = FISH_AUCTION",
            { it.actionType == CONTROL_SECTOR && it.sectorType == SectorType.FISHING && it.sectorEstablishmentType == SectorEstablishmentType.FISH_AUCTION },
            relatedActionTypes = listOf(CONTROL_SECTOR), extraCondition = "sectorType = FISHING et sectorEstablishmentType = FISH_AUCTION") { it.fishAuction },
        conditional("portLocode", "Le port est requis",
            "actionType = CONTROL_SECTOR et sectorType = FISHING et sectorEstablishmentType = LANDING_SITE",
            { it.actionType == CONTROL_SECTOR && it.sectorType == SectorType.FISHING && it.sectorEstablishmentType == SectorEstablishmentType.LANDING_SITE },
            relatedActionTypes = listOf(CONTROL_SECTOR), extraCondition = "sectorType = FISHING et sectorEstablishmentType = LANDING_SITE") { it.portLocode },

        // CONTROL_SLEEPING_FISHING_GEAR-specific
        Rule.forActionTypes("fishingGearType", listOf(CONTROL_SLEEPING_FISHING_GEAR), "Le type d'engin de pêche est requis") { it.fishingGearType },

        // OTHER_CONTROL-specific
        Rule.forActionTypes("controlType", listOf(OTHER_CONTROL), "Le type de contrôle est requis") { it.controlType },

        // SECURITY_VISIT-specific
        Rule.forActionTypes("securityVisitType", listOf(SECURITY_VISIT), "Le type de visite de sécurité est requis") { it.securityVisitType },
        Rule.forActionTypes("nbrSecurityVisit", listOf(SECURITY_VISIT), "Le nombre de visites de sécurité est requis") { it.nbrSecurityVisit },

        // RESCUE-specific
        conditional("numberPersonsRescued", "Le nombre de personnes secourues est requis",
            "actionType = RESCUE et isPersonRescue = true",
            { it.actionType == RESCUE && it.isPersonRescue == true },
            relatedActionTypes = listOf(RESCUE), extraCondition = "isPersonRescue = true") { it.numberPersonsRescued },

        conditional("numberOfDeaths", "Le nombre de décès est requis",
            "actionType = RESCUE et isPersonRescue = true",
            { it.actionType == RESCUE && it.isPersonRescue == true },
            relatedActionTypes = listOf(RESCUE), extraCondition = "isPersonRescue = true") { it.numberOfDeaths },

        conditional("nbOfVesselsTrackedWithoutIntervention", "Le nombre de navires suivis sans intervention est requis",
            "actionType = RESCUE et isMigrationRescue = true",
            { it.actionType == RESCUE && it.isMigrationRescue == true },
            relatedActionTypes = listOf(RESCUE), extraCondition = "isMigrationRescue = true") { it.nbOfVesselsTrackedWithoutIntervention },

        conditional("nbAssistedVesselsReturningToShore", "Le nombre de navires assistés retournant à terre est requis",
            "actionType = RESCUE et isMigrationRescue = true",
            { it.actionType == RESCUE && it.isMigrationRescue == true },
            relatedActionTypes = listOf(RESCUE), extraCondition = "isMigrationRescue = true") { it.nbAssistedVesselsReturningToShore },
    )

    // =========================================================================
    // MissionEnvActionEntity rules
    // =========================================================================
    private val envActionRules: List<Rule<MissionEnvActionEntity>> = listOf(
        Rule.always("startDateTimeUtc", MSG_START_DATE_REQUIRED) { it.startDateTimeUtc },
        Rule.always("endDateTimeUtc", MSG_END_DATE_REQUIRED) { it.endDateTimeUtc }
    )

    // =========================================================================
    // MissionFishActionEntity rules
    // =========================================================================
    private val fishActionRules: List<Rule<MissionFishActionEntity>> = listOf(
        Rule.always("startDateTimeUtc", MSG_START_DATE_REQUIRED) { it.startDateTimeUtc },
        Rule.always("endDateTimeUtc", MSG_END_DATE_REQUIRED) { it.endDateTimeUtc }
    )

    // =========================================================================
    // Policies
    // =========================================================================

    val v1 = ValidationPolicy(
        version = 1,
        label = "Initial rules",
        appliesFrom = Instant.parse("2025-01-01T00:00:00Z"),
        rules = mapOf(
            MissionGeneralInfoEntity::class.java to generalInfoRules,
            MissionNavActionEntity::class.java to navActionRules,
            MissionEnvActionEntity::class.java to envActionRules,
            MissionFishActionEntity::class.java to fishActionRules
        )
    )

    /** All policies, newest first. */
    val all: List<ValidationPolicy> = listOf(v1)

    /** The most recent policy. */
    val latest: ValidationPolicy get() = all.first()

    /**
     * Resolves the applicable policy for a mission based on its start date.
     * Returns the newest policy whose [ValidationPolicy.appliesFrom] is on or before [startDate].
     * Falls back to the oldest policy if the mission predates all policies.
     */
    fun forMissionStartDate(startDate: Instant?): ValidationPolicy {
        if (startDate == null) return latest
        return all.firstOrNull { !startDate.isBefore(it.appliesFrom) } ?: all.last()
    }
}
