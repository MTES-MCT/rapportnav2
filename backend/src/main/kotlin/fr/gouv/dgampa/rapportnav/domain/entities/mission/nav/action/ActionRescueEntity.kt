package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.NavActionRescue
import java.time.Instant
import java.util.*

data class ActionRescueEntity(
    @MandatoryForStats
    override val id: UUID,

    @MandatoryForStats
    override val missionId: String,

    override var isCompleteForStats: Boolean? = null,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,

    @MandatoryForStats
    override val startDateTimeUtc: Instant,
    @MandatoryForStats
    override val endDateTimeUtc: Instant? = null,

    @MandatoryForStats
    val latitude: Float? = null,
    @MandatoryForStats
    val longitude: Float? = null,

    val isVesselRescue: Boolean? = false,
    val isPersonRescue: Boolean? = false,


    val isVesselNoticed: Boolean? = false,
    val isVesselTowed: Boolean? = false,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            ),
        ]
    )
    val numberPersonsRescued: Int? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            ),
        ]
    )
    val numberOfDeaths: Int? = null,


    val operationFollowsDEFREP: Boolean? = false,
    override val observations: String? = null,
    val locationDescription: String? = null,


    val isMigrationRescue: Boolean? = false,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
        ]
    )
    val nbAssistedVesselsReturningToShore: Int? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
        ]
    )
    val nbOfVesselsTrackedWithoutIntervention: Int? = null,
) : BaseAction {

    constructor(
        id: UUID,
        missionId: String,
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
        latitude: Float? = null,
        longitude: Float? = null,
        isVesselRescue: Boolean? = false,
        isPersonRescue: Boolean? = false,
        isVesselNoticed: Boolean? = false,
        isVesselTowed: Boolean? = false,
        isInSRRorFollowedByCROSSMRCC: Boolean? = false,
        numberPersonsRescued: Int? = null,
        numberOfDeaths: Int? = null,
        operationFollowsDEFREP: Boolean? = false,
        observations: String? = null,
        locationDescription: String? = null,
        isMigrationRescue: Boolean? = false,
        nbAssistedVesselsReturningToShore: Int? = null,
        nbOfVesselsTrackedWithoutIntervention: Int? = null,
    ) : this(
        id = id,
        missionId = missionId,
        isCompleteForStats = null,
        startDateTimeUtc = startDateTimeUtc,
        endDateTimeUtc = endDateTimeUtc,
        latitude = latitude,
        longitude = longitude,
        isVesselRescue = isVesselRescue,
        isPersonRescue = isPersonRescue,
        isVesselNoticed = isVesselNoticed,
        isVesselTowed = isVesselTowed,
        isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
        numberPersonsRescued = numberPersonsRescued,
        numberOfDeaths = numberOfDeaths,
        operationFollowsDEFREP = operationFollowsDEFREP,
        observations = observations,
        locationDescription = locationDescription,
        isMigrationRescue = isMigrationRescue,
        nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
        nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention,
    ) {
        // completeness for stats being computed at class instantiation in constructor
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
    }


    fun toNavActionEntity(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            isCompleteForStats = isCompleteForStats,
            sourcesOfMissingDataForStats = sourcesOfMissingDataForStats,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.RESCUE,
            rescueAction = this
        )
    }

    fun toNavActionRescue(): NavActionRescue {
        return NavActionRescue(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isVesselTowed = isVesselTowed,
            isVesselNoticed = isVesselNoticed,
            isVesselRescue = isVesselRescue,
            isPersonRescue = isPersonRescue,
            observations = observations,
            numberOfDeaths = numberOfDeaths,
            operationFollowsDEFREP = operationFollowsDEFREP,
            latitude = latitude,
            longitude = longitude,
            isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
            numberPersonsRescued = numberPersonsRescued,
            locationDescription = locationDescription,
            isMigrationRescue = isMigrationRescue,
            nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
            nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention
        )
    }
}
