package fr.gouv.dgampa.rapportnav.domain.validation

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * Generic validator for required fields.
 *
 * Rules are defined per entity class in the companion object.
 * To add rules for a new entity, add an entry to the `rulesRegistry` map.
 */
class RequiredFieldsValidator : ConstraintValidator<RequiredFields, Any> {

    override fun isValid(entity: Any?, context: ConstraintValidatorContext): Boolean {
        if (entity == null) return true

        val rules = rulesRegistry[entity::class.java] ?: return true
        val violations = mutableListOf<FieldViolation>()

        for (rule in rules) {
            if (rule.appliesTo(entity) && rule.isViolated(entity)) {
                violations.add(FieldViolation(rule.field, rule.message))
            }
        }

        if (violations.isEmpty()) return true

        context.disableDefaultConstraintViolation()
        for (violation in violations) {
            context.buildConstraintViolationWithTemplate(violation.message)
                .addPropertyNode(violation.field)
                .addConstraintViolation()
        }

        return false
    }

    data class FieldViolation(val field: String, val message: String)

    companion object {
        // =====================================================================
        // MissionGeneralInfoEntity rules
        // =====================================================================
        private val generalInfoRules: List<Rule<MissionGeneralInfoEntity>> = listOf(
            Rule.always("distanceInNauticalMiles", "La distance en milles nautiques est requise") { it.distanceInNauticalMiles },
            Rule.always("consumedGOInLiters", "La consommation de GO en litres est requise") { it.consumedGOInLiters },
            Rule.always("consumedFuelInLiters", "La consommation de carburant en litres est requise") { it.consumedFuelInLiters },
            Rule.always("nbrOfRecognizedVessel", "Le nombre de navires reconnus est requis") { it.nbrOfRecognizedVessel }
        )

        // =====================================================================
        // MissionNavActionEntity rules
        // =====================================================================
        private val ACTION_TYPES_REQUIRING_END_DATE = listOf(
            ANTI_POLLUTION, BAAEM_PERMANENCE, CONTROL, RESCUE,
            VIGIMER, REPRESENTATION, PUBLIC_ORDER, ILLEGAL_IMMIGRATION, NAUTICAL_EVENT,
            CONDUCT_HEARING, COMMUNICATION, TRAINING, UNIT_MANAGEMENT_PLANNING, UNIT_MANAGEMENT_TRAINING,
            CONTROL_SECTOR, CONTROL_NAUTICAL_LEISURE, CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL,
            RESOURCES_MAINTENANCE, MEETING, PV_DRAFTING, HEARING_CONDUCT, LAND_SURVEILLANCE,
            FISHING_SURVEILLANCE, UNIT_MANAGEMENT_OTHER, OTHER, MARITIME_SURVEILLANCE
        )

        private val ACTION_TYPES_REQUIRING_LOCATION = listOf(
            CONTROL, RESCUE, ILLEGAL_IMMIGRATION, ANTI_POLLUTION,
            CONTROL_SLEEPING_FISHING_GEAR, OTHER_CONTROL
        )

        private val navActionRules: List<Rule<MissionNavActionEntity>> = listOf(
            // Always required
            Rule.always("id", "L'identifiant est requis") { it.id },
            Rule.always("missionId", "L'identifiant de mission est requis") { it.missionId },
            Rule.always("actionType", "Le type d'action est requis") { it.actionType },
            Rule.always("startDateTimeUtc", "La date de début est requise") { it.startDateTimeUtc },

            // Required for specific actionTypes
            Rule.forActionTypes("endDateTimeUtc", ACTION_TYPES_REQUIRING_END_DATE, "La date de fin est requise") { it.endDateTimeUtc },
            Rule.forActionTypes("latitude", ACTION_TYPES_REQUIRING_LOCATION, "La latitude est requise") { it.latitude },
            Rule.forActionTypes("longitude", ACTION_TYPES_REQUIRING_LOCATION, "La longitude est requise") { it.longitude },

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
            Rule.conditional("reason", "La raison est requise",
                { it.actionType == STATUS && (it.status == ActionStatusType.DOCKED || it.status == ActionStatusType.UNAVAILABLE) }) { it.reason },

            // INQUIRY-specific
            Rule.forActionTypes("nbrOfHours", listOf(INQUIRY), "Le nombre d'heures est requis") { it.nbrOfHours },

            // TRAINING-specific
            Rule.forActionTypes("trainingType", listOf(TRAINING), "Le type de formation est requis") { it.trainingType },
            Rule.forActionTypes("unitManagementTrainingType", listOf(UNIT_MANAGEMENT_TRAINING), "Le type de formation est requis") { it.unitManagementTrainingType },

            // RESOURCES_MAINTENANCE-specific
            Rule.forActionTypes("resourceType", listOf(RESOURCES_MAINTENANCE), "Le type de ressource est requis") { it.resourceType },
            Rule.forActionTypes("resourceId", listOf(RESOURCES_MAINTENANCE), "L'identifiant de ressource est requis") { it.resourceId },

            // CONTROL_NAUTICAL_LEISURE-specific
            Rule.forActionTypes("nbrOfControl", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles est requis") { it.nbrOfControl },
            Rule.forActionTypes("nbrOfControlAmp", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles AMP est requis") { it.nbrOfControlAmp },
            Rule.forActionTypes("nbrOfControl300m", listOf(CONTROL_NAUTICAL_LEISURE), "Le nombre de contrôles 300m est requis") { it.nbrOfControl300m },
            Rule.forActionTypes("leisureType", listOf(CONTROL_NAUTICAL_LEISURE), "Le type de loisir est requis") { it.leisureType },

            // CONTROL_SECTOR-specific
            Rule.forActionTypes("establishment", listOf(CONTROL_SECTOR), "L'établissement est requis") { it.establishment },
            Rule.forActionTypes("sectorType", listOf(CONTROL_SECTOR), "Le type de secteur est requis") { it.sectorType },
            Rule.forActionTypes("sectorEstablishmentType", listOf(CONTROL_SECTOR), "Le type d'établissement est requis") { it.sectorEstablishmentType },

            // CONTROL_SLEEPING_FISHING_GEAR-specific
            Rule.forActionTypes("fishingGearType", listOf(CONTROL_SLEEPING_FISHING_GEAR), "Le type d'engin de pêche est requis") { it.fishingGearType },

            // OTHER_CONTROL-specific
            Rule.forActionTypes("controlType", listOf(OTHER_CONTROL), "Le type de contrôle est requis") { it.controlType },

            // SECURITY_VISIT-specific
            Rule.forActionTypes("securityVisitType", listOf(SECURITY_VISIT), "Le type de visite de sécurité est requis") { it.securityVisitType },
            Rule.forActionTypes("nbrSecurityVisit", listOf(SECURITY_VISIT), "Le nombre de visites de sécurité est requis") { it.nbrSecurityVisit },

            // RESCUE-specific
            Rule.conditional("numberPersonsRescued", "Le nombre de personnes secourues est requis",
                { it.actionType == RESCUE && it.isPersonRescue == true }) { it.numberPersonsRescued },

            Rule.conditional("numberOfDeaths", "Le nombre de décès est requis",
                { it.actionType == RESCUE && it.isPersonRescue == true }) { it.numberOfDeaths },

            Rule.conditional("nbOfVesselsTrackedWithoutIntervention", "Le nombre de navires suivis sans intervention est requis",
                { it.actionType == RESCUE && it.isMigrationRescue == true }) { it.nbOfVesselsTrackedWithoutIntervention },

            Rule.conditional("nbAssistedVesselsReturningToShore", "Le nombre de navires assistés retournant à terre est requis",
                { it.actionType == RESCUE && it.isMigrationRescue == true }) { it.nbAssistedVesselsReturningToShore },


        )

        // =====================================================================
        // Rules registry - maps entity classes to their validation rules
        // =====================================================================
        val rulesRegistry: Map<Class<*>, List<Rule<*>>> = mapOf(
            MissionGeneralInfoEntity::class.java to generalInfoRules,
            MissionNavActionEntity::class.java to navActionRules
        )
    }

    /**
     * Represents a validation rule for a field.
     */
    sealed class Rule<T>(
        val field: String,
        val message: String
    ) {
        abstract fun appliesTo(entity: Any): Boolean
        abstract fun isViolated(entity: Any): Boolean

        class Always<T>(
            field: String,
            message: String,
            private val getter: (T) -> Any?
        ) : Rule<T>(field, message) {
            override fun appliesTo(entity: Any) = true
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as T) == null
        }

        class ForActionTypes(
            field: String,
            private val actionTypes: List<ActionType>,
            message: String,
            private val getter: (MissionNavActionEntity) -> Any?
        ) : Rule<MissionNavActionEntity>(field, message) {
            override fun appliesTo(entity: Any) = (entity as? MissionNavActionEntity)?.actionType in actionTypes
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as MissionNavActionEntity) == null
        }

        class Conditional<T>(
            field: String,
            message: String,
            private val condition: (T) -> Boolean,
            private val getter: (T) -> Any?
        ) : Rule<T>(field, message) {
            @Suppress("UNCHECKED_CAST")
            override fun appliesTo(entity: Any) = condition(entity as T)
            @Suppress("UNCHECKED_CAST")
            override fun isViolated(entity: Any) = getter(entity as T) == null
        }

        companion object {
            fun <T> always(field: String, message: String, getter: (T) -> Any?) =
                Always(field, message, getter)

            fun forActionTypes(field: String, actionTypes: List<ActionType>, message: String, getter: (MissionNavActionEntity) -> Any?) =
                ForActionTypes(field, actionTypes, message, getter)

            fun <T> conditional(field: String, message: String, condition: (T) -> Boolean, getter: (T) -> Any?) =
                Conditional(field, message, condition, getter)
        }
    }
}
