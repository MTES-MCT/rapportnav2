package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.config.DependentFieldValue
import fr.gouv.dgampa.rapportnav.config.MandatoryForStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
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

    override var ownerId: UUID? = null,

    @MandatoryForStats
    override var actionType: ActionType,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    @MandatoryForStats
    override var startDateTimeUtc: Instant? = null,
    @MandatoryForStats(
        enableIf = [DependentFieldValue(
            field = "actionType",
            value = ["ANTI_POLLUTION", "BAAEM_PERMANENCE", "CONTROL", "RESCUE",
                "VIGIMER", "REPRESENTATION", "PUBLIC_ORDER", "ILLEGAL_IMMIGRATION", "NAUTICAL_EVENT",
                "CONDUCT_HEARING", "COMMUNICATION", "TRAINING", "UNIT_MANAGEMENT_PLANNING", "UNIT_MANAGEMENT_TRAINING",
                "CONTROL_SECTOR", "CONTROL_NAUTICAL_LEISURE", "CONTROL_SLEEPING_FISHING_GEAR", "OTHER_CONTROL",
                "RESOURCES_MAINTENANCE", "MEETING", "PV_DRAFTING", "HEARING_CONDUCT", "LAND_SURVEILLANCE", "FISHING_SURVEILLANCE", "UNIT_MANAGEMENT_OTHER", "OTHER", "MARITIME_SURVEILLANCE"]
        )]
    )
    override var endDateTimeUtc: Instant? = null,
    override var observations: String? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "actionType",
                value = ["CONTROL", "RESCUE", "ILLEGAL_IMMIGRATION", "ANTI_POLLUTION", "CONTROL_SLEEPING_FISHING_GEAR", "OTHER_CONTROL"
                ])
        ]
    )
    override var latitude: Double? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "actionType",
                value = ["CONTROL", "RESCUE", "ILLEGAL_IMMIGRATION", "ANTI_POLLUTION", "CONTROL_SLEEPING_FISHING_GEAR", "OTHER_CONTROL"
                ])
        ]
    )
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
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            )
        ]
    )
    override var numberPersonsRescued: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isPersonRescue",
                value = arrayOf("true")
            ),
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
        ]
    )
    override var numberOfDeaths: Int? = null,
    override var operationFollowsDEFREP: Boolean? = false,
    override var locationDescription: String? = null,
    override var isMigrationRescue: Boolean? = false,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
            DependentFieldValue(field = "actionType", value = ["RESCUE"])
        ]
    )
    override var nbOfVesselsTrackedWithoutIntervention: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(
                field = "isMigrationRescue",
                value = arrayOf("true")
            ),
            DependentFieldValue(field = "actionType", value = ["RESCUE"]),
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
            DependentFieldValue(
                field = "status",
                value = arrayOf(DOCKED_STATUS_AS_STRING, UNAVAILABLE_STATUS_AS_STRING)
            ),
            DependentFieldValue(field = "actionType", value = ["STATUS"])
        ]
    )

    override var reason: ActionStatusReason? = null,
    override var targets: List<TargetEntity>? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["INQUIRY"])
        ]
    )
    override var nbrOfHours: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["TRAINING"])
        ]
    )
    override var trainingType: String? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["UNIT_MANAGEMENT_TRAINING"])
        ]
    )
    override var unitManagementTrainingType: String? = null,
    override var isWithinDepartment: Boolean? = null,
    override var hasDivingDuringOperation: Boolean? = null,
    override var incidentDuringOperation: Boolean? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESOURCES_MAINTENANCE"])
        ]
    )
    override var resourceType: String? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["RESOURCES_MAINTENANCE"])
        ]
    )
    override var resourceId: Int? = null,

    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_NAUTICAL_LEISURE"])
        ]
    )
    override var nbrOfControl: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_SECTOR"])
        ]
    )
    override var establishment: EstablishmentEntity? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_SECTOR"])
        ]
    )
    override val sectorType: SectorType? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_NAUTICAL_LEISURE"])
        ]
    )
    override var nbrOfControlAmp: Int? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_NAUTICAL_LEISURE"])
        ]
    )
    override var nbrOfControl300m: Int? = null,
    override var isControlDuringSecurityDay: Boolean? = null,
    override var isSeizureSleepingFishingGear: Boolean? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_SECTOR"])
        ]
    )
    override var sectorEstablishmentType: SectorEstablishmentType? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_NAUTICAL_LEISURE"])
        ]
    )
    override var leisureType: LeisureType? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["CONTROL_SLEEPING_FISHING_GEAR"])
        ]
    )
    override var fishingGearType: FishingGearType? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["OTHER_CONTROL"])
        ]
    )
    override var controlType: String? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["SECURITY_VISIT"])
        ]
    )
    override var securityVisitType:SecurityVisitType? = null,
    @MandatoryForStats(
        enableIf = [
            DependentFieldValue(field = "actionType", value = ["SECURITY_VISIT"])
        ]
    )
    override var nbrSecurityVisit:Int? = null,
) : MissionActionEntity(
    status = status,
    actionType = actionType,
    missionId = missionId,
    isCompleteForStats = false,
    endDateTimeUtc = endDateTimeUtc,
    startDateTimeUtc = startDateTimeUtc,
    source = MissionSourceEnum.RAPPORT_NAV,
    targets = targets
), BaseMissionNavAction {

    override fun getActionId(): String {
        return id.toString()
    }

    override fun computeCompleteness() {
        this.isCompleteForStats = EntityCompletenessValidator.isCompleteForStats(this)
        this.sourcesOfMissingDataForStats =
            if (this.isCompleteForStats == true) emptyList()
            else listOf(MissionSourceEnum.RAPPORT_NAV)

        this.computeSummaryTags()
        this.computeCompletenessForStats()
    }

    fun toMissionActionModel() = MissionActionModel(
        id = id,
        missionId = missionId,
        actionType = actionType,
        isCompleteForStats = isCompleteForStats,
        startDateTimeUtc = startDateTimeUtc ?: Instant.now(),
        endDateTimeUtc = endDateTimeUtc,
        observations = observations,
        latitude = latitude,
        longitude = longitude,
        detectedPollution = detectedPollution,
        pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
        diversionCarriedOut = diversionCarriedOut,
        isSimpleBrewingOperationDone = isSimpleBrewingOperationDone,
        isAntiPolDeviceDeployed = isAntiPolDeviceDeployed,
        controlMethod = controlMethod?.toString(),
        vesselIdentifier = vesselIdentifier,
        vesselType = vesselType?.toString(),
        vesselSize = vesselSize?.toString(),
        identityControlledPerson = identityControlledPerson,
        nbOfInterceptedVessels = nbOfInterceptedVessels,
        nbOfInterceptedMigrants = nbOfInterceptedMigrants,
        nbOfSuspectedSmugglers = nbOfSuspectedSmugglers,
        isVesselRescue = isVesselRescue,
        isPersonRescue = isPersonRescue,
        isVesselNoticed = isVesselNoticed,
        isVesselTowed = isVesselTowed,
        isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
        numberPersonsRescued = numberPersonsRescued,
        numberOfDeaths = numberOfDeaths,
        operationFollowsDEFREP = operationFollowsDEFREP,
        locationDescription = locationDescription,
        isMigrationRescue = isMigrationRescue,
        nbOfVesselsTrackedWithoutIntervention = nbOfVesselsTrackedWithoutIntervention,
        nbAssistedVesselsReturningToShore = nbAssistedVesselsReturningToShore,
        status = status?.toString(),
        reason = reason?.toString(),
        ownerId = ownerId,
        nbrOfHours = nbrOfHours,
        trainingType = trainingType,
        unitManagementTrainingType = unitManagementTrainingType,
        hasDivingDuringOperation = hasDivingDuringOperation,
        incidentDuringOperation = incidentDuringOperation,
        resourceType = resourceType,
        resourceId = resourceId,
        nbrOfControl = nbrOfControl,
        sectorType = sectorType?.toString(),
        nbrOfControlAmp = nbrOfControlAmp,
        nbrOfControl300m = nbrOfControl300m,
        isControlDuringSecurityDay = isControlDuringSecurityDay,
        isSeizureSleepingFishingGear = isSeizureSleepingFishingGear,
        sectorEstablishmentType = sectorEstablishmentType?.toString(),
        leisureType = leisureType?.toString(),
        fishingGearType = fishingGearType?.toString(),
        controlType = controlType,
        nbrSecurityVisit = nbrSecurityVisit,
        securityVisitType = securityVisitType?.toString(),
        establishment = establishment?.toEstablishmentModel(),
        isWithinDepartment =  if(isWithinDepartment == null)  true else isWithinDepartment
    )


    override fun isControlInValid(control: ControlEntity?): Boolean {
        TODO("Not yet implemented")
    }

    companion object {
        fun fromMissionActionModel(model: MissionActionModel): MissionNavActionEntity {
            return MissionNavActionEntity(
                id = model.id,
                missionId = model.missionId ?: 0,
                actionType = model.actionType,
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
                reason = model.reason?.let { ActionStatusReason.valueOf(it) },
                ownerId = model.ownerId,
                nbrOfHours = model.nbrOfHours,
                trainingType = model.trainingType,
                unitManagementTrainingType = model.unitManagementTrainingType,
                isWithinDepartment = model.isWithinDepartment,
                hasDivingDuringOperation = model.hasDivingDuringOperation,
                incidentDuringOperation = model.incidentDuringOperation,
                resourceType = model.resourceType,
                resourceId = model.resourceId,
                nbrOfControl = model.nbrOfControl,
                sectorType = model.sectorType?.let { SectorType.valueOf(it) },
                nbrOfControlAmp = model.nbrOfControlAmp,
                nbrOfControl300m = model.nbrOfControl300m,
                isControlDuringSecurityDay = model.isControlDuringSecurityDay,
                isSeizureSleepingFishingGear = model.isSeizureSleepingFishingGear,
                sectorEstablishmentType = model.sectorEstablishmentType?.let { SectorEstablishmentType.valueOf(it) },
                leisureType = model.leisureType?.let { LeisureType.valueOf(it) },
                fishingGearType = model.fishingGearType?.let { FishingGearType.valueOf(it) },
                controlType = model.controlType,
                nbrSecurityVisit = model.nbrSecurityVisit,
                securityVisitType = model.securityVisitType?.let { SecurityVisitType.valueOf(it) },
                establishment = model.establishment?.let { EstablishmentEntity.fromEstablishmentModel(it) }
            )
        }
    }
}
