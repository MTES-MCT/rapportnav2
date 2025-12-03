package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

class MissionNavAction(
    override val id: String? = null,
    override val missionId: Int,
    override val ownerId: String? = null,
    override val actionType: ActionType,
    override val source: MissionSourceEnum,
    override val summaryTags: List<String>? = null,
    override var status: ActionStatusType? = null,
    override val isCompleteForStats: Boolean? = null,
    override val controlsToComplete: List<ControlType>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionNavActionData,
) : MissionAction(
    id = id.toString(),
    missionId = missionId,
    ownerId = ownerId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    source = source,
    controlsToComplete = controlsToComplete,
    data = data,
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionNavAction {
            val navAction = action as MissionNavActionEntity
            return MissionNavAction(
                id = navAction.id.toString(),
                missionId = navAction.missionId,
                ownerId = navAction.ownerId?.toString(),
                status = navAction.status,
                source = navAction.source,
                actionType = navAction.actionType,
                summaryTags = navAction.summaryTags,
                completenessForStats = navAction.completenessForStats,
                isCompleteForStats = navAction.isCompleteForStats,
                controlsToComplete = navAction.controlsToComplete,
                data = MissionNavActionData(
                    startDateTimeUtc = navAction.startDateTimeUtc,
                    endDateTimeUtc = navAction.endDateTimeUtc,
                    observations = navAction.observations,
                    latitude = navAction.latitude,
                    longitude = navAction.longitude,
                    detectedPollution = navAction.detectedPollution,
                    pollutionObservedByAuthorizedAgent = navAction.pollutionObservedByAuthorizedAgent,
                    diversionCarriedOut = navAction.diversionCarriedOut,
                    isSimpleBrewingOperationDone = navAction.isSimpleBrewingOperationDone,
                    isAntiPolDeviceDeployed = navAction.isAntiPolDeviceDeployed,
                    controlMethod = navAction.controlMethod,
                    vesselIdentifier = navAction.vesselIdentifier,
                    vesselType = navAction.vesselType,
                    vesselSize = navAction.vesselSize,
                    identityControlledPerson = navAction.identityControlledPerson,
                    nbOfInterceptedVessels = navAction.nbOfInterceptedVessels,
                    nbOfInterceptedMigrants = navAction.nbOfInterceptedMigrants,
                    nbOfSuspectedSmugglers = navAction.nbOfSuspectedSmugglers,
                    isVesselRescue = navAction.isVesselRescue,
                    isPersonRescue = navAction.isPersonRescue,
                    isVesselNoticed = navAction.isVesselNoticed,
                    isVesselTowed = navAction.isVesselTowed,
                    isInSRRorFollowedByCROSSMRCC = navAction.isInSRRorFollowedByCROSSMRCC,
                    numberPersonsRescued = navAction.numberPersonsRescued,
                    numberOfDeaths = navAction.numberOfDeaths,
                    operationFollowsDEFREP = navAction.operationFollowsDEFREP,
                    locationDescription = navAction.locationDescription,
                    isMigrationRescue = navAction.isMigrationRescue,
                    nbOfVesselsTrackedWithoutIntervention = navAction.nbOfVesselsTrackedWithoutIntervention,
                    nbAssistedVesselsReturningToShore = navAction.nbAssistedVesselsReturningToShore,
                    status = if (navAction.actionType == ActionType.STATUS) navAction.status else null, // status only set for ActionType Status
                    reason = navAction.reason,
                    nbrOfHours = navAction.nbrOfHours,
                    trainingType = navAction.trainingType,
                    unitManagementTrainingType = navAction.unitManagementTrainingType,
                    isWithinDepartment = navAction.isWithinDepartment,
                    hasDivingDuringOperation = navAction.hasDivingDuringOperation,
                    incidentDuringOperation = navAction.incidentDuringOperation,
                    resourceId = navAction.resourceId,
                    resourceType = navAction.resourceType,
                    nbrOfControl = navAction.nbrOfControl,
                    sectorType = navAction.sectorType,
                    nbrOfControlAmp = navAction.nbrOfControlAmp,
                    nbrOfControl300m = navAction.nbrOfControl300m,
                    isControlDuringSecurityDay = navAction.isControlDuringSecurityDay,
                    isSeizureSleepingFishingGear = navAction.isSeizureSleepingFishingGear,
                    sectorEstablishmentType = navAction.sectorEstablishmentType,
                    leisureType = navAction.leisureType,
                    fishingGearType = navAction.fishingGearType,
                    controlType = navAction.controlType,
                    securityVisitType = navAction.securityVisitType,
                    nbrSecurityVisit = navAction.nbrSecurityVisit,
                    establishment = navAction.establishment?.let { Establishment.fromEstablishmentEntity(it) },
                    targets = navAction.targets?.map { Target2.fromTargetEntity(it) }?.sortedBy { it.startDateTimeUtc }
                )
            )
        }
    }
}

