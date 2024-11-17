package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity

class MissionNavActionOutput(
    override val id: String,
    override val missionId: Int,
    override val actionType: ActionType,
    override val source: MissionSourceEnum,
    override val summaryTags: List<String>? = null,
    override val status: ActionStatusType? = null,
    override val isCompleteForStats: Boolean? = null,
    override val controlsToComplete: List<ControlType>? = null,
    override val completenessForStats: CompletenessForStatsEntity? = null,
    override val sourcesOfMissingDataForStats: List<MissionSourceEnum>? = null,
    override val data: MissionNavActionDataOutput
) : MissionActionOutput(
    id = id.toString(),
    missionId = missionId,
    status = status,
    actionType = actionType,
    summaryTags = summaryTags,
    source = source,
    controlsToComplete = controlsToComplete,
    data = data
) {
    companion object {
        fun fromMissionActionEntity(action: MissionActionEntity): MissionNavActionOutput {
            val navAction = action as MissionNavActionEntity
            return MissionNavActionOutput(
                id = navAction.id.toString(),
                status = navAction.status,
                source = navAction.source,
                missionId = navAction.missionId,
                actionType = navAction.actionType,
                summaryTags = navAction.summaryTags,
                completenessForStats = navAction.completenessForStats,
                isCompleteForStats = navAction.isCompleteForStats,
                controlsToComplete = navAction.controlsToComplete,
                data = MissionNavActionDataOutput(
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
                    reason = navAction.reason,
                    controlSecurity = navAction.controlSecurity,
                    controlGensDeMer = navAction.controlGensDeMer,
                    controlNavigation = navAction.controlNavigation,
                    controlAdministrative = navAction.controlAdministrative
                )
            )
        }
    }
}

