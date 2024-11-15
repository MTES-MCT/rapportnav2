package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.DOCKED_STATUS_AS_STRING
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.UNAVAILABLE_STATUS_AS_STRING
import fr.gouv.dgampa.rapportnav.domain.utils.EntityCompletenessValidator
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import java.time.Instant
import java.util.*

class MissionNavActionEntity(
    @MandatoryForStats
    override var id: UUID,

    @MandatoryForStats
    override var missionId: Int,

    @MandatoryForStats
    override var actionType: ActionType,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    @MandatoryForStats
    override var startDateTimeUtc: Instant? =  null,
    @MandatoryForStats(
        enableIf = [DependentFieldValue(
            field = "actionType",
            value = ["ANTI_POLLUTION", "BAAEM_PERMANENCE", "CONTROL",
                "VIGIMER", "REPRESENTATION", "PUBLIC_ORDER", "ILLEGAL_IMMIGRATION", "NAUTICAL_EVENT"]
        )]
    )
    override var endDateTimeUtc: Instant? = null,
    override var observations: String? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL", "RESCUE"])])
    override var latitude: Double? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL", "RESCUE"])])
    override var longitude: Double? = null,
    override var detectedPollution: Boolean? = null,
    override var pollutionObservedByAuthorizedAgent: Boolean? = null,
    override var diversionCarriedOut: Boolean? = null,
    override var isSimpleBrewingOperationDone: Boolean? = null,
    override var isAntiPolDeviceDeployed: Boolean? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL"])])
    override var controlMethod: ControlMethod? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL"])])
    override var vesselIdentifier: String? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL"])])
    override var vesselType: VesselTypeEnum? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL"])])
    override var vesselSize: VesselSizeEnum? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["CONTROL"])])
    override var identityControlledPerson: String? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["ILLEGAL_IMMIGRATION"])])
    override var nbOfInterceptedVessels: Int? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["ILLEGAL_IMMIGRATION"])])
    override var nbOfInterceptedMigrants: Int? = null,
    @MandatoryForStats(enableIf = [DependentFieldValue(field = "actionType", value = ["ILLEGAL_IMMIGRATION"])])
    override var nbOfSuspectedSmugglers: Int? = null,

    override var isVesselRescue: Boolean? = false,
    override var isPersonRescue: Boolean? = false,
    override var isVesselNoticed: Boolean? = false,
    override var isVesselTowed: Boolean? = false,
    override var isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            ),
        ]
    )
    override var numberPersonsRescued: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            )
        ]
    )
    override var numberOfDeaths: Int? = null,
    override var operationFollowsDEFREP: Boolean? = false,
    override var locationDescription: String? = null,
    override var isMigrationRescue: Boolean? = false,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
        ]
    )
    override var nbOfVesselsTrackedWithoutIntervention: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
        ]
    )
    override var nbAssistedVesselsReturningToShore: Int? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["STATUS"])
        ]
    )
    override var status: ActionStatusType? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["STATUS"]),
            DependentFieldValue(
                field = "status",
                value = arrayOf(DOCKED_STATUS_AS_STRING, UNAVAILABLE_STATUS_AS_STRING)
            ),
        ]
    )
    override var reason: ActionStatusReason? = null,
) : MissionActionEntity(
    actionType = actionType,
    missionId = missionId,
    isCompleteForStats = false,
    endDateTimeUtc = endDateTimeUtc,
    startDateTimeUtc = startDateTimeUtc,
    source = MissionSourceEnum.RAPPORTNAV
), BaseMissionNavAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeCompleteness() {
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORTNAV)
        this.computeSummaryTags()
        this.computeCompletenessForStats()
    }

    companion object {
        fun fromMissionActionModel(model: MissionActionModel): MissionNavActionEntity {
            return MissionNavActionEntity(
                id = model.id,
                missionId = model.missionId,
                actionType = model.actionType.let { ActionType.valueOf(it) },
                startDateTimeUtc = model.startDateTimeUtc,
                endDateTimeUtc = model.endDateTimeUtc,
                observations = model.observations,
                latitude = model.latitude,
                longitude = model.longitude,
                detectedPollution = model.detectedPollution,
                pollutionObservedByAuthorizedAgent = model.pollutionObservedByAuthorizedAgent,
                diversionCarriedOut = model.diversionCarriedOut,
                isSimpleBrewingOperationDone = model.isSimpleBrewingOperationDone,
                isAntiPolDeviceDeployed = model.isAntiPolDeviceDeployed,
                controlMethod = model.controlMethod?.let { ControlMethod.valueOf(it) },
                vesselIdentifier = model.vesselIdentifier,
                vesselType = model.vesselType?.let { VesselTypeEnum.valueOf(it) },
                vesselSize = model.vesselSize?.let { VesselSizeEnum.valueOf(it) },
                identityControlledPerson = model.identityControlledPerson,
                nbOfInterceptedVessels = model.nbOfInterceptedVessels,
                nbOfInterceptedMigrants = model.nbOfInterceptedMigrants,
                nbOfSuspectedSmugglers = model.nbOfSuspectedSmugglers,
                isVesselRescue = model.isVesselRescue,
                isPersonRescue = model.isPersonRescue,
                isVesselNoticed = model.isVesselNoticed,
                isVesselTowed = model.isVesselTowed,
                isInSRRorFollowedByCROSSMRCC = model.isInSRRorFollowedByCROSSMRCC,
                numberPersonsRescued = model.numberPersonsRescued,
                numberOfDeaths = model.numberOfDeaths,
                operationFollowsDEFREP = model.operationFollowsDEFREP,
                locationDescription = model.locationDescription,
                isMigrationRescue = model.isMigrationRescue,
                nbOfVesselsTrackedWithoutIntervention = model.nbOfVesselsTrackedWithoutIntervention,
                nbAssistedVesselsReturningToShore = model.nbAssistedVesselsReturningToShore,
                status = model.status?.let { ActionStatusType.valueOf(it) },
                reason = model.reason?.let { ActionStatusReason.valueOf(it) }
            )
        }
    }
}
