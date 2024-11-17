package fr.gouv.gmampa.rapportnav.domain.entities.mission


import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.gmampa.rapportnav.mocks.mission.action.ControlMock
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
        assertThat(entity.actionType.toString()).isEqualTo(model.actionType)
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
    fun `execute set status and controls`() {
        val model = getActionModel()
        val mockControl  = ControlMock.createAllControl()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.computeControls(controls = mockControl)
        assertThat(entity.controlGensDeMer).isEqualTo(mockControl.controlGensDeMer)
        assertThat(entity.controlSecurity).isEqualTo(mockControl.controlSecurity)
        assertThat(entity.controlNavigation).isEqualTo(mockControl.controlNavigation)
        assertThat(entity.controlAdministrative).isEqualTo(mockControl.controlAdministrative)
    }

    @Test
    fun `execute should compute summary tags`() {
        val model = getActionModel()
        val mockControl  = ControlMock.createAllControl()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        entity.computeControls(controls = mockControl)
        entity.computeSummaryTags()
        assertThat(entity.summaryTags).isNotNull()
        assertThat(entity.summaryTags?.get(0)).isEqualTo("1 PV")
        assertThat(entity.summaryTags?.get(1)).isEqualTo("Sans infraction")
    }

    private fun getActionModel(): MissionActionModel{
        return MissionActionModelMock.create()
    }
}
