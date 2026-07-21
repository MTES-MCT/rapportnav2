package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.LocationType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.EstablishmentModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.fish.FishAuctionModel
import java.time.Instant
import java.util.*

object MissionActionModelMock {
    fun create(
        id: UUID = UUID.fromString("0000-00-00-00-000000"),
        missionId: Int? = 761,
        ownerId: UUID? = UUID.randomUUID(),
        actionType: ActionType = ActionType.CONTROL,
        status: String? = ActionStatusType.ANCHORED.toString(),
        startDateTimeUtc: Instant = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc: Instant? = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        observations: String? = "observations",
        isAntiPolDeviceDeployed: Boolean? = true,
        isSimpleBrewingOperationDone: Boolean? = true,
        diversionCarriedOut: Boolean? = true,
        vesselType: String? = VesselTypeEnum.FISHING.toString(),
        controlMethod: String? = ControlMethod.SEA.toString(),
        locationType: String? = LocationType.GPS.toString(),
        reason: String? = null,
        fishAuction: FishAuctionModel? = null,
    ): MissionActionModel {
        return MissionActionModel(
            missionId = missionId,
            id = id,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = observations,
            isAntiPolDeviceDeployed = isAntiPolDeviceDeployed,
            isSimpleBrewingOperationDone = isSimpleBrewingOperationDone,
            diversionCarriedOut = diversionCarriedOut,
            actionType = actionType,
            latitude = 3434.0,
            longitude = 4353.0,
            detectedPollution = false,
            pollutionObservedByAuthorizedAgent = false,
            controlMethod = controlMethod,
            locationType = locationType,
            vesselIdentifier = "FR-23566",
            vesselType = vesselType,
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
            reason = reason,
            status = status,
            ownerId = ownerId,
            nbrOfHours = 65,
            trainingType = "FORMATION_BCP",
            unitManagementTrainingType = "DIVING",
            hasDivingDuringOperation = false,
            incidentDuringOperation = false,
            isWithinDepartment = true,
            resourceType = "NAUTICAL",
            resourceIds = listOf(345),
            nbrOfControl = 34,
            sectorType = SectorType.FISHING.toString(),
            nbrOfControlAmp = 4,
            nbrOfControl300m = 3,
            isControlDuringSecurityDay = false,
            isSeizureSleepingFishingGear = true,
            sectorEstablishmentType = SectorEstablishmentType.FISH_AUCTION.toString(),
            leisureType = LeisureType.KAYAK.toString(),
            fishingGearType = FishingGearType.CASHIER.toString(),
            controlType = "my control type",
            nbrSecurityVisit = 12,
            securityVisitType = SecurityVisitType.SCHOOL_BOAT.toString(),
            fishAuction = fishAuction,
            establishment = EstablishmentModel(
                id = 2,
                name = "myEstablishment",
                address = "myAddress",
                isForeign = false,
                city = "Paris",
                siren = "mySiren",
                zipcode = "18733"
            )
        )
    }
}
