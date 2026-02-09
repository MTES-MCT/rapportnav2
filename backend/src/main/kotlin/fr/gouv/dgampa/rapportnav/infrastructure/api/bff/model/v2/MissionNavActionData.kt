package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
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
    override val targets: List<Target>? = null,
    override val nbrOfHours: Int? = null,
    override val trainingType: String? = null,
    override val unitManagementTrainingType: String? = null,
    override val isWithinDepartment: Boolean? = null,
    override val hasDivingDuringOperation: Boolean? = null,
    override val incidentDuringOperation: Boolean? = null,
    override val resourceType: String? = null,
    override val resourceId: Int? = null,
    override var nbrOfControl: Int? = null,
    override var controlType: String? = null,
    override val sectorType: SectorType? = null,
    override var nbrOfControlAmp: Int? = null,
    override var nbrOfControl300m: Int? = null,
    override var leisureType: LeisureType? = null,
    override var fishingGearType: FishingGearType? = null,
    override var isControlDuringSecurityDay: Boolean? = null,
    override var isSeizureSleepingFishingGear: Boolean? = null,
    override var sectorEstablishmentType: SectorEstablishmentType? = null,
    override var nbrSecurityVisit:Int? = null,
    override var securityVisitType: SecurityVisitType? = null,
    override var establishment: Establishment? = null
) : MissionActionData(
    startDateTimeUtc = startDateTimeUtc,
    endDateTimeUtc = endDateTimeUtc,
    observations = observations,
    targets = targets
), BaseMissionNavActionData {

    companion object {
        fun toMissionNavActionEntity(input: MissionAction): NavActionEntity {
            val data = input.data as MissionNavActionData
            val action  = NavActionEntity(
                id = if(input.id == null) UUID.randomUUID() else UUID.fromString(input.id),
                ownerId = input.ownerId?.let { UUID.fromString(it) },
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
                nbrOfHours = data.nbrOfHours,
                trainingType = data.trainingType,
                unitManagementTrainingType = data.unitManagementTrainingType,
                isWithinDepartment = data.isWithinDepartment,
                hasDivingDuringOperation = data.hasDivingDuringOperation,
                incidentDuringOperation = data.incidentDuringOperation,
                resourceType = data.resourceType,
                resourceId = data.resourceId,
                nbrOfControl = data.nbrOfControl,
                sectorType = data.sectorType,
                nbrOfControlAmp = data.nbrOfControlAmp,
                nbrOfControl300m = data.nbrOfControl300m,
                isControlDuringSecurityDay = data.isControlDuringSecurityDay,
                isSeizureSleepingFishingGear = data.isSeizureSleepingFishingGear,
                sectorEstablishmentType = data.sectorEstablishmentType,
                leisureType = data.leisureType,
                fishingGearType = data.fishingGearType,
                controlType = data.controlType,
                securityVisitType = data.securityVisitType,
                nbrSecurityVisit = data.nbrSecurityVisit,
                establishment = data.establishment?.toEstablishmentEntity()
            )
            return action
        }
    }
}
