package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossedControlConclusionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.CrossedControlStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import java.time.Instant
import java.util.*

class MissionNavActionData(
    override val latitude: Double?,
    override val longitude: Double?,
    override val detectedPollution: Boolean?,
    override val pollutionObservedByAuthorizedAgent: Boolean?,
    override val diversionCarriedOut: Boolean?,
    override val isSimpleBrewingOperationDone: Boolean?,
    override val isAntiPolDeviceDeployed: Boolean?,
    override val controlMethod: ControlMethod?,
    override val vesselIdentifier: String?,
    override val vesselType: VesselTypeEnum?,
    override val vesselSize: VesselSizeEnum?,
    override val identityControlledPerson: String?,
    override val nbOfInterceptedVessels: Int?,
    override val nbOfInterceptedMigrants: Int?,
    override val nbOfSuspectedSmugglers: Int?,
    override val isVesselRescue: Boolean?,
    override val isPersonRescue: Boolean?,
    override val isVesselNoticed: Boolean?,
    override val isVesselTowed: Boolean?,
    override val isInSRRorFollowedByCROSSMRCC: Boolean?,
    override val numberPersonsRescued: Int?,
    override val numberOfDeaths: Int?,
    override val operationFollowsDEFREP: Boolean?,
    override val locationDescription: String?,
    override val isMigrationRescue: Boolean?,
    override val nbOfVesselsTrackedWithoutIntervention: Int?,
    override val nbAssistedVesselsReturningToShore: Int?,
    override val reason: ActionStatusReason?,
    override val startDateTimeUtc: Instant? = null,
    override val endDateTimeUtc: Instant? = null,
    override val observations: String? = null,
    override val status: ActionStatusType? = null,
    override val targets: List<Target2>? = null,
    override val crossedControlId: String? = null,
    override val isSignedByInspector: Boolean? = null,
    override val crossedControlNbrOfHours: Int? = null,
    override val crossedControlStatus: CrossedControlStatusType? = null,
    override val crossedControlConclusion: CrossedControlConclusionType? = null,
) : MissionActionData(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    observations = observations,
    targets = targets
), BaseMissionNavActionData {
    companion object {
        fun toMissionNavActionEntity(input: MissionAction): MissionNavActionEntity {
            val data = input.data as MissionNavActionData
            val action  = MissionNavActionEntity(
                id = if(input.id == null) UUID.randomUUID() else UUID.fromString(input.id),
                missionId = input.missionId,
                actionType = input.actionType,
                startDateTimeUtc = data.startDateTimeUtc,
                endDateTimeUtc = data.endDateTimeUtc,
                observations = data.observations,
                latitude = data.latitude,
                longitude = data.longitude,
                detectedPollution = data.detectedPollution,
                pollutionObservedByAuthorizedAgent = data.pollutionObservedByAuthorizedAgent,
                diversionCarriedOut = data.diversionCarriedOut,
                isSimpleBrewingOperationDone = data.isSimpleBrewingOperationDone,
                isAntiPolDeviceDeployed = data.isAntiPolDeviceDeployed,
                controlMethod = data.controlMethod,
                vesselIdentifier = data.vesselIdentifier,
                vesselType = data.vesselType,
                vesselSize = data.vesselSize,
                identityControlledPerson = data.identityControlledPerson,
                nbOfInterceptedVessels = data.nbOfInterceptedVessels,
                nbOfInterceptedMigrants = data.nbOfInterceptedMigrants,
                nbOfSuspectedSmugglers = data.nbOfSuspectedSmugglers,
                isVesselRescue = data.isVesselRescue,
                isPersonRescue = data.isPersonRescue,
                isVesselNoticed = data.isVesselNoticed,
                isVesselTowed = data.isVesselTowed,
                isInSRRorFollowedByCROSSMRCC = data.isInSRRorFollowedByCROSSMRCC,
                numberPersonsRescued = data.numberPersonsRescued,
                numberOfDeaths = data.numberOfDeaths,
                operationFollowsDEFREP = data.operationFollowsDEFREP,
                locationDescription = data.locationDescription,
                isMigrationRescue = data.isMigrationRescue,
                nbOfVesselsTrackedWithoutIntervention = data.nbOfVesselsTrackedWithoutIntervention,
                nbAssistedVesselsReturningToShore = data.nbAssistedVesselsReturningToShore,
                status = data.status,
                reason = data.reason,
                crossedControlId = data.crossedControlId,
                isSignedByInspector = data.isSignedByInspector,
                crossedControlNbrOfHours = data.crossedControlNbrOfHours,
                crossedControlStatus = data.crossedControlStatus,
                crossedControlConclusion = data.crossedControlConclusion
            )
            return action
        }
    }
}
