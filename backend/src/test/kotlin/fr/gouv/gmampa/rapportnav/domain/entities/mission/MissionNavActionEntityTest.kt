package fr.gouv.gmampa.rapportnav.domain.entities.mission


import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionNavActionEntityTest {

    @Test
    fun `execute should retrieve entity  from model`() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)

        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(model.id)
        assertThat(entity.missionId).isEqualTo(model.missionId)
        assertThat(entity.startDateTimeUtc).isEqualTo(model.startDateTimeUtc)
        assertThat(entity.endDateTimeUtc).isEqualTo(model.endDateTimeUtc)
        assertThat(entity.observations).isEqualTo(model.observations)
        assertThat(entity.isAntiPolDeviceDeployed).isEqualTo(model.isAntiPolDeviceDeployed)
        assertThat(entity.isSimpleBrewingOperationDone).isEqualTo(model.isSimpleBrewingOperationDone)
        assertThat(entity.diversionCarriedOut).isEqualTo(model.diversionCarriedOut)
        assertThat(entity.actionType).isEqualTo(model.actionType)
        assertThat(entity.latitude).isEqualTo(model.latitude)
        assertThat(entity.longitude).isEqualTo(model.longitude)
        assertThat(entity.detectedPollution).isEqualTo(model.detectedPollution)
        assertThat(entity.pollutionObservedByAuthorizedAgent).isEqualTo(model.pollutionObservedByAuthorizedAgent)
        assertThat(entity.controlMethod.toString()).isEqualTo(model.controlMethod)
        assertThat(entity.vesselIdentifier).isEqualTo(model.vesselIdentifier)
        assertThat(entity.vesselType.toString()).isEqualTo(model.vesselType)
        assertThat(entity.vesselSize.toString()).isEqualTo(model.vesselSize)
        assertThat(entity.identityControlledPerson).isEqualTo(model.identityControlledPerson)
        assertThat(entity.nbOfInterceptedVessels).isEqualTo(model.nbOfInterceptedVessels)
        assertThat(entity.nbOfInterceptedMigrants).isEqualTo(model.nbOfInterceptedMigrants)
        assertThat(entity.nbOfSuspectedSmugglers).isEqualTo(model.nbOfSuspectedSmugglers)
        assertThat(entity.isVesselRescue).isEqualTo(model.isVesselRescue)
        assertThat(entity.isPersonRescue).isEqualTo(model.isPersonRescue)
        assertThat(entity.isVesselNoticed).isEqualTo(model.isVesselNoticed)
        assertThat(entity.isVesselTowed).isEqualTo(model.isVesselTowed)
        assertThat(entity.isInSRRorFollowedByCROSSMRCC).isEqualTo(model.isInSRRorFollowedByCROSSMRCC)
        assertThat(entity.numberPersonsRescued).isEqualTo(model.numberPersonsRescued)
        assertThat(entity.numberOfDeaths).isEqualTo(model.numberOfDeaths)
        assertThat(entity.operationFollowsDEFREP).isEqualTo(model.operationFollowsDEFREP)
        assertThat(entity.locationDescription).isEqualTo(model.locationDescription)
        assertThat(entity.isMigrationRescue).isEqualTo(model.isMigrationRescue)
        assertThat(entity.nbOfVesselsTrackedWithoutIntervention).isEqualTo(model.nbOfVesselsTrackedWithoutIntervention)
        assertThat(entity.nbAssistedVesselsReturningToShore).isEqualTo(model.nbAssistedVesselsReturningToShore)
        assertThat(entity.status.toString()).isEqualTo(model.status)
        assertThat(entity.reason.toString()).isEqualTo(model.reason)
        assertThat(entity.nbrOfHours).isEqualTo(model.nbrOfHours)
        assertThat(entity.trainingType).isEqualTo(model.trainingType)
        assertThat(entity.unitManagementTrainingType).isEqualTo(model.unitManagementTrainingType)
        assertThat(entity.hasDivingDuringOperation).isEqualTo(model.hasDivingDuringOperation)
        assertThat(entity.incidentDuringOperation).isEqualTo(model.incidentDuringOperation)
        assertThat(entity.isWithinDepartment).isEqualTo(model.isWithinDepartment)
        assertThat(entity.resourceId).isEqualTo(model.resourceId)
        assertThat(entity.resourceType).isEqualTo(model.resourceType)

        assertThat(entity.nbrSecurityVisit).isEqualTo(model.nbrSecurityVisit)
        assertThat(entity.securityVisitType.toString()).isEqualTo(model.securityVisitType)

        assertThat(entity.siren).isEqualTo(model.siren)
        assertThat(entity.controlType).isEqualTo(model.controlType)
        assertThat(entity.nbrOfControl).isEqualTo(model.nbrOfControl)
        assertThat(entity.sectorType.toString()).isEqualTo(model.sectorType)
        assertThat(entity.nbrOfControlAmp).isEqualTo(model.nbrOfControlAmp)
        assertThat(entity.nbrOfControl300m).isEqualTo(model.nbrOfControl300m)
        assertThat(entity.isControlDuringSecurityDay).isEqualTo(model.isControlDuringSecurityDay)
        assertThat(entity.isSeizureSleepingFishingGear).isEqualTo(model.isSeizureSleepingFishingGear)
        assertThat(entity.sectorEstablishmentType.toString()).isEqualTo(model.sectorEstablishmentType)
        assertThat(entity.leisureType.toString()).isEqualTo(model.leisureType)
        assertThat(entity.fishingGearType.toString()).isEqualTo(model.fishingGearType)
    }

    @Test
    fun `execute should retrieve action model from entity`() {
        val entity = MissionNavActionEntity(
            missionId = 761,
            id = UUID.fromString("0000-00-00-00-000000"),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            latitude = 3434.0,
            longitude = 4353.0,
            detectedPollution = false,
            pollutionObservedByAuthorizedAgent = false,
            controlMethod = ControlMethod.SEA,
            vesselIdentifier = "vesselIdentifier",
            vesselType = VesselTypeEnum.FISHING,
            vesselSize = VesselSizeEnum.LESS_THAN_12m,
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
            reason = ActionStatusReason.ADMINISTRATION,
            status = ActionStatusType.ANCHORED,
            ownerId = UUID.randomUUID(),
            nbrOfHours = 45,
            siren = "mySiren",
            nbrOfControl = 34,
            sectorType = SectorType.FISHING,
            nbrOfControlAmp = 4,
            nbrOfControl300m = 3,
            isControlDuringSecurityDay = false,
            isSeizureSleepingFishingGear = true,
            sectorEstablishmentType = SectorEstablishmentType.SHOUTED,
            leisureType = LeisureType.KAYAK,
            fishingGearType = FishingGearType.CASHIER,
            controlType = "my control type",
        )
        val model = entity.toMissionActionModel()

        assertThat(model).isNotNull()
        assertThat(model.id).isEqualTo(entity.id)
        assertThat(model.missionId).isEqualTo(entity.missionId)
        assertThat(model.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(model.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(model.observations).isEqualTo(entity.observations)
        assertThat(model.isAntiPolDeviceDeployed).isEqualTo(entity.isAntiPolDeviceDeployed)
        assertThat(model.isSimpleBrewingOperationDone).isEqualTo(entity.isSimpleBrewingOperationDone)
        assertThat(model.diversionCarriedOut).isEqualTo(entity.diversionCarriedOut)
        assertThat(model.actionType).isEqualTo(entity.actionType)
        assertThat(model.latitude).isEqualTo(entity.latitude)
        assertThat(model.longitude).isEqualTo(entity.longitude)
        assertThat(model.detectedPollution).isEqualTo(entity.detectedPollution)
        assertThat(model.pollutionObservedByAuthorizedAgent).isEqualTo(entity.pollutionObservedByAuthorizedAgent)
        assertThat(model.controlMethod).isEqualTo(entity.controlMethod.toString())
        assertThat(model.vesselIdentifier).isEqualTo(entity.vesselIdentifier)
        assertThat(model.vesselType).isEqualTo(entity.vesselType.toString())
        assertThat(model.vesselSize).isEqualTo(entity.vesselSize.toString())
        assertThat(model.identityControlledPerson).isEqualTo(entity.identityControlledPerson)
        assertThat(model.nbOfInterceptedVessels).isEqualTo(entity.nbOfInterceptedVessels)
        assertThat(model.nbOfInterceptedMigrants).isEqualTo(entity.nbOfInterceptedMigrants)
        assertThat(model.nbOfSuspectedSmugglers).isEqualTo(entity.nbOfSuspectedSmugglers)
        assertThat(model.isVesselRescue).isEqualTo(entity.isVesselRescue)
        assertThat(model.isPersonRescue).isEqualTo(entity.isPersonRescue)
        assertThat(model.isVesselNoticed).isEqualTo(entity.isVesselNoticed)
        assertThat(model.isVesselTowed).isEqualTo(entity.isVesselTowed)
        assertThat(model.isInSRRorFollowedByCROSSMRCC).isEqualTo(entity.isInSRRorFollowedByCROSSMRCC)
        assertThat(model.numberPersonsRescued).isEqualTo(entity.numberPersonsRescued)
        assertThat(model.numberOfDeaths).isEqualTo(entity.numberOfDeaths)
        assertThat(model.operationFollowsDEFREP).isEqualTo(entity.operationFollowsDEFREP)
        assertThat(model.locationDescription).isEqualTo(entity.locationDescription)
        assertThat(model.isMigrationRescue).isEqualTo(entity.isMigrationRescue)
        assertThat(model.nbOfVesselsTrackedWithoutIntervention).isEqualTo(entity.nbOfVesselsTrackedWithoutIntervention)
        assertThat(model.nbAssistedVesselsReturningToShore).isEqualTo(entity.nbAssistedVesselsReturningToShore)
        assertThat(model.status).isEqualTo(entity.status.toString())
        assertThat(model.reason).isEqualTo(entity.reason.toString())
        assertThat(model.ownerId).isEqualTo(entity.ownerId)
        assertThat(model.nbrOfHours).isEqualTo(entity.nbrOfHours)

        assertThat(model.trainingType).isEqualTo(entity.trainingType)
        assertThat(model.unitManagementTrainingType).isEqualTo(entity.unitManagementTrainingType)
        assertThat(model.hasDivingDuringOperation).isEqualTo(entity.hasDivingDuringOperation)
        assertThat(model.incidentDuringOperation).isEqualTo(entity.incidentDuringOperation)
        assertThat(model.isWithinDepartment).isEqualTo(entity.isWithinDepartment)

        assertThat(model.resourceId).isEqualTo(entity.resourceId)
        assertThat(model.resourceType).isEqualTo(entity.resourceType)
        assertThat(model.siren).isEqualTo(entity.siren)
        assertThat(model.controlType).isEqualTo(entity.controlType)
        assertThat(model.nbrOfControl).isEqualTo(entity.nbrOfControl)
        assertThat(model.sectorType).isEqualTo(entity.sectorType.toString())
        assertThat(model.nbrOfControlAmp).isEqualTo(entity.nbrOfControlAmp)
        assertThat(model.nbrOfControl300m).isEqualTo(entity.nbrOfControl300m)
        assertThat(model.isControlDuringSecurityDay).isEqualTo(entity.isControlDuringSecurityDay)
        assertThat(model.isSeizureSleepingFishingGear).isEqualTo(entity.isSeizureSleepingFishingGear)
        assertThat(model.sectorEstablishmentType).isEqualTo(entity.sectorEstablishmentType.toString())
        assertThat(model.leisureType).isEqualTo(entity.leisureType.toString())
        assertThat(model.fishingGearType).isEqualTo(entity.fishingGearType.toString())

        assertThat(model.nbrSecurityVisit).isEqualTo(entity.nbrSecurityVisit)
        assertThat(model.securityVisitType).isEqualTo(entity.securityVisitType.toString())

    }

    @Test
    fun `execute should be complete for stats `() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.computeCompleteness()
        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.sourcesOfMissingDataForStats).isEqualTo(emptyList<MissionSourceEnum>())
        assertThat(entity.completenessForStats?.sources).isEqualTo(emptyList<MissionSourceEnum>())
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }

    @Test
    fun `execute should return infraction type tag`() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        assertThat(entity.getInfractionTag(6)).isEqualTo("6 PV")
        assertThat(entity.getInfractionTag(0)).isEqualTo("Sans PV")
    }

    @Test
    fun `execute should return natinf tag`() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        assertThat(entity.getNatinfTag(7)).isEqualTo("7 NATINF")
        assertThat(entity.getNatinfTag(0)).isEqualTo("Sans infraction")
    }

    @Test
    fun `execute should compute summary tags`() {
        val model = getActionModel()
        val controls  = listOf(ControlMock.create(controlType = ControlType.SECURITY))
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.targets = listOf(TargetMissionMock.create(controls = controls))
        entity.computeSummaryTags()
        assertThat(entity.summaryTags).isNotNull()
        assertThat(entity.summaryTags?.get(0)).isEqualTo("1 PV")
        assertThat(entity.summaryTags?.get(1)).isEqualTo("2 NATINF")
    }

    private fun getActionModel(): MissionActionModel{
        return MissionActionModelMock.create()
    }
}
