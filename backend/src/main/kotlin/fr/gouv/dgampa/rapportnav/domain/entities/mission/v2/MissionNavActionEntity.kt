package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.FishAuctionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.LocationType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.validation.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import jakarta.validation.constraints.Min
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish.FishAuctionModel
import java.time.Instant
import java.util.*

@EndAfterStart(groups = [ValidateThrowsBeforeSave::class])
@WithinMissionDateRange(groups = [ValidateThrowsBeforeSave::class])
@RequiredFields(groups = [ValidateWhenMissionFinished::class])
class MissionNavActionEntity(
    override var id: UUID,

    override var missionId: Int,

    override var ownerId: UUID? = null,

    override var actionType: ActionType,
    override var sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override var startDateTimeUtc: Instant? = null,
    override var endDateTimeUtc: Instant? = null,
    override var observations: String? = null,
    override var latitude: Double? = null,
    override var longitude: Double? = null,
    override var detectedPollution: Boolean? = null,
    override var pollutionObservedByAuthorizedAgent: Boolean? = null,
    override var diversionCarriedOut: Boolean? = null,
    override var isSimpleBrewingOperationDone: Boolean? = null,
    override var isAntiPolDeviceDeployed: Boolean? = null,
    override var controlMethod: ControlMethod? = null,
    override var locationType: LocationType? = null,
    override var vesselIdentifier: String? = null,
    override var vesselType: VesselTypeEnum? = null,
    override var vesselSize: VesselSizeEnum? = null,
    override var identityControlledPerson: String? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbOfInterceptedVessels: Int? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class], message = "Le nombre de migrants interceptés doit être supérieur à 0")
    override var nbOfInterceptedMigrants: Int? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbOfSuspectedSmugglers: Int? = null,

    override var isVesselRescue: Boolean? = false,
    override var isPersonRescue: Boolean? = false,
    override var isVesselNoticed: Boolean? = false,
    override var isVesselTowed: Boolean? = false,
    override var isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var numberPersonsRescued: Int? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var numberOfDeaths: Int? = null,
    override var operationFollowsDEFREP: Boolean? = false,
    override var locationDescription: String? = null,
    override var isMigrationRescue: Boolean? = false,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbOfVesselsTrackedWithoutIntervention: Int? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbAssistedVesselsReturningToShore: Int? = null,

    override var status: ActionStatusType? = null,

    override var reason: ActionStatusReason? = null,
    override var targets: List<TargetEntity>? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbrOfHours: Int? = null,
    override var trainingType: String? = null,
    override var unitManagementTrainingType: String? = null,
    override var isWithinDepartment: Boolean? = null,
    override var hasDivingDuringOperation: Boolean? = null,
    override var incidentDuringOperation: Boolean? = null,
    override var resourceType: String? = null,

    override var resourceId: Int? = null,

    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbrOfControl: Int? = null,

    override var establishment: EstablishmentEntity? = null,

    override var portLocode: String? = null,


    override var zipCode: String? = null,
    override var city: String? = null,

    override var fishAuction: FishAuctionEntity? = null,

    override val sectorType: SectorType? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbrOfControlAmp: Int? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
    override var nbrOfControl300m: Int? = null,
    override var isControlDuringSecurityDay: Boolean? = null,
    override var isSeizureSleepingFishingGear: Boolean? = null,
    override var sectorEstablishmentType: SectorEstablishmentType? = null,
    override var leisureType: LeisureType? = null,
    override var fishingGearType: FishingGearType? = null,
    override var controlType: String? = null,
    override var securityVisitType:SecurityVisitType? = null,
    @field:Min(value = 0, groups = [ValidateThrowsBeforeSave::class])
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

    /**
     * Computes validity for statistics using the new unified validation system.
     * Uses Jakarta Bean Validation with validation groups.
     *
     * @param isMissionFinished When true, also checks required fields (ValidateWhenMissionFinished group)
     * @param validator The EntityValidityValidator instance to use
     */
    override fun computeValidity(isMissionFinished: Boolean, validator: EntityValidityValidator) {
        this.computeValidityForStats(isMissionFinished, validator)

        // For Nav actions, the only source is RAPPORT_NAV
        if (this.completenessForStats?.isComplete != true) {
            this.sourcesOfMissingDataForStats = listOf(MissionSourceEnum.RAPPORT_NAV)
            this.isCompleteForStats = false
        }

        this.computeSummaryTags()
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
        locationType = locationType?.toString(),
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
        portLocode = portLocode,
        zipCode = zipCode,
        city = city,
        fishAuction = fishAuction?.let { FishAuctionModel.fromFishAuctionEntity(it) },
        isWithinDepartment = isWithinDepartment ?: true
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
                locationType = model.locationType?.let { LocationType.valueOf(it) },
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
                establishment = model.establishment?.let { EstablishmentEntity.fromEstablishmentModel(it) },
                portLocode = model.portLocode,
                zipCode = model.zipCode,
                city = model.city,
                fishAuction = model.fishAuction?.toFishAuctionEntity(),
            )
        }
    }
}
