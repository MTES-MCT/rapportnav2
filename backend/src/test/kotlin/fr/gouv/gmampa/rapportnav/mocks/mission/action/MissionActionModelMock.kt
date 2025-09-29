package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import java.time.Instant
import java.util.*

object MissionActionModelMock {
    fun create(
        actionType: ActionType = ActionType.CONTROL,
        status: ActionStatusType? = ActionStatusType.ANCHORED,
        startDateTimeUtc: Instant = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        observations: String? = "observations",

    ): MissionActionModel {
        return MissionActionModel(
            missionId = 761,
            id = UUID.fromString("0000-00-00-00-000000"),
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = observations,
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = actionType,
            latitude = 3434.0,
            longitude = 4353.0,
            detectedPollution = false,
            pollutionObservedByAuthorizedAgent = false,
            controlMethod = ControlMethod.SEA.toString(),
            vesselIdentifier = "FR-23566",
            vesselType = VesselTypeEnum.FISHING.toString(),
            vesselSize = VesselSizeEnum.LESS_THAN_12m.toString(),
            identityControlledPerson = "identityControlledPerson",
            nbOfInterceptedVessels = 4,
            nbOfInterceptedMigrants = 64,
            nbOfSuspectedSmugglers = 67,
            isVesselRescue = false,
            isPersonRescue = true,
            isVesselNoticed = true,
            isVesselTowed = true,
            isInSRRorFollowedByCROSSMRCC = false,
            numberPersonsRescued = 4,
            numberOfDeaths = 90,
            operationFollowsDEFREP = false,
            locationDescription = "locationDescription",
            isMigrationRescue = false,
            nbOfVesselsTrackedWithoutIntervention = 4,
            nbAssistedVesselsReturningToShore = 50,
            reason = ActionStatusReason.ADMINISTRATION.toString(),
            status = status.toString(),
            ownerId = UUID.randomUUID(),
            nbrOfHours = 65,
            trainingType = "FORMATION_BCP",
            unitManagementTrainingType = "DIVING",
            hasDivingDuringOperation = false,
            isWithinDepartment = true,
            resourceType = "NAUTICAL",
            resourceId = 345,
            siren = "mySiren",
            nbrOfControl = 34,
            sectorType = SectorType.FISHING.toString(),
            nbrOfControlAmp = 4,
            nbrOfControl300m = 3,
            isControlDuringSecurityDay = false,
            isSeizureSleepingFishingGear = true,
            sectorEstablishmentType = SectorEstablishmentType.SHOUTED?.toString(),
            leisureType = LeisureType.KAYAK?.toString(),
            fishingGearType = FishingGearType.CASHIER?.toString(),
            controlType = "my control type"
        )
    }
}
