package fr.gouv.gmampa.rapportnav.domain.entities.mission


import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusReason
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
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
        assertThat(entity.actionType).isEqualTo(model.actionType.toString())
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
        assertThat(entity.reason.toString()).isEqualTo(model.reason)
    }


    @Test
    fun `execute should retrieve retrieve timeline out put from entity`() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        val timelineOutput = entity.toMissionActionTimelineOutput()

        assertThat(timelineOutput).isNotNull()
        assertThat(timelineOutput.id).isEqualTo(entity.id.toString())
        assertThat(timelineOutput.missionId).isEqualTo(entity.missionId)
        assertThat(timelineOutput.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(timelineOutput.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(timelineOutput.observations).isEqualTo(entity.observations)
        assertThat(timelineOutput.controlMethod).isEqualTo(entity.controlMethod)
        assertThat(timelineOutput.vesselIdentifier).isEqualTo(entity.vesselIdentifier)
        assertThat(timelineOutput.vesselType).isEqualTo(entity.vesselType)
        assertThat(timelineOutput.vesselSize).isEqualTo(entity.vesselSize)
        assertThat(timelineOutput.isVesselRescue).isEqualTo(entity.isVesselRescue)
        assertThat(timelineOutput.isPersonRescue).isEqualTo(entity.isPersonRescue)
        assertThat(timelineOutput.reason).isEqualTo(entity.reason)
    }

    @Test
    fun `execute should be complete for stats `() {
        val model = getActionModel()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.computeCompleteness()

        assertThat(entity.isCompleteForStats).isEqualTo(true)
        assertThat(entity.sourcesOfMissingDataForStats).isEqualTo(listOf(MissionSourceEnum.RAPPORTNAV))
        assertThat(entity.completenessForStats?.sources).isEqualTo(listOf(MissionSourceEnum.RAPPORTNAV))
        assertThat(entity.completenessForStats?.status).isEqualTo(CompletenessForStatsStatusEnum.COMPLETE)
    }


    private fun getActionModel(): MissionActionModel{
        return MissionActionModel(
            missionId = 761,
            id = UUID.fromString("0000-00-00-00-000000"),
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL.toString(),
            latitude = 3434.0,
            longitude = 4353.0,
            detectedPollution = false,
            pollutionObservedByAuthorizedAgent = false,
            controlMethod = ControlMethod.SEA.toString(),
            vesselIdentifier = "vesselIdentifier",
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
            reason = ActionStatusReason.ADMINISTRATION.toString()
        )
    }
}
