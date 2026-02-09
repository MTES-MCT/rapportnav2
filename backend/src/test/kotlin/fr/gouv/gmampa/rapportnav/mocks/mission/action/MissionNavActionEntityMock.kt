package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import java.time.Instant
import java.util.*

object MissionNavActionEntityMock {
    fun create(
        missionId: Int? = null,
        id: UUID? = null,
        startDateTimeUtc: Instant? = null,
        endDateTimeUtc: Instant? = null,
        observations: String? = null,
        actionType: ActionType? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        detectedPollution: Boolean? = null,
        pollutionObservedByAuthorizedAgent: Boolean? = null,
        diversionCarriedOut: Boolean? = null,
        isSimpleBrewingOperationDone: Boolean? = null,
        isAntiPolDeviceDeployed: Boolean? = null,
        controlMethod: ControlMethod? = null,
        vesselIdentifier: String? = null,
        vesselType: VesselTypeEnum? = null,
        vesselSize: VesselSizeEnum? = null,
        identityControlledPerson: String? = null,
        nbOfInterceptedVessels: Int? = null,
        nbOfInterceptedMigrants: Int? = null,
        nbOfSuspectedSmugglers: Int? = null,
        isVesselRescue: Boolean? = false,
        isPersonRescue: Boolean? = false,
        isVesselNoticed: Boolean? = false,
        isVesselTowed: Boolean? = false,
        isInSRRorFollowedByCROSSMRCC: Boolean? = false,
        numberPersonsRescued: Int? = null,
        numberOfDeaths: Int? = null,
        operationFollowsDEFREP: Boolean? = false,
        locationDescription: String? = null,
        isMigrationRescue: Boolean? = false,
        nbOfVesselsTrackedWithoutIntervention: Int? = null,
        nbAssistedVesselsReturningToShore: Int? = null,
        status: ActionStatusType? = null,
        reason: ActionStatusReason? = null,
        targets: List<TargetEntity>? = null
    ): NavActionEntity {
        return NavActionEntity(
            missionId = missionId?: 761,
            id = id?: UUID.randomUUID(),
            startDateTimeUtc = startDateTimeUtc?: Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            isAntiPolDeviceDeployed = isAntiPolDeviceDeployed,
            isSimpleBrewingOperationDone = isSimpleBrewingOperationDone,
            diversionCarriedOut = diversionCarriedOut,
            actionType = actionType?: ActionType.CONTROL,
            latitude = latitude,
            longitude = longitude,
            detectedPollution = detectedPollution,
            pollutionObservedByAuthorizedAgent = pollutionObservedByAuthorizedAgent,
            controlMethod = controlMethod,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
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
            reason = reason,
            status = status,
            targets = targets
        )
    }
}
