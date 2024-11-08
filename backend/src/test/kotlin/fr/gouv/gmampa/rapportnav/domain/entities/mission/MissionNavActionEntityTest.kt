package fr.gouv.gmampa.rapportnav.domain.entities.mission


import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

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
        return MissionActionModelMock.create()
    }
}
