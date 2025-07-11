package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionNavActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
class MissionNavActionDataTest {

    @Test
    fun `execute should retrieve output from mission action nav Entity`() {
        val model = MissionActionModelMock.create()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        val output = MissionNavAction.fromMissionActionEntity(entity)

        assertThat(entity).isNotNull()
        assertThat(entity.id).isEqualTo(entity.id)
        assertThat(output.status).isEqualTo(entity.status)
        assertThat(entity.missionId).isEqualTo(entity.missionId)
        assertThat(output.summaryTags).isEqualTo(entity.summaryTags)
        assertThat(output.data.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(output.data.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(output.data.observations).isEqualTo(entity.observations)
        assertThat(output.data.isAntiPolDeviceDeployed).isEqualTo(entity.isAntiPolDeviceDeployed)
        assertThat(output.data.isSimpleBrewingOperationDone).isEqualTo(entity.isSimpleBrewingOperationDone)
        assertThat(output.data.diversionCarriedOut).isEqualTo(entity.diversionCarriedOut)
        assertThat(output.actionType).isEqualTo(entity.actionType)
        assertThat(output.data.latitude).isEqualTo(entity.latitude)
        assertThat(output.data.longitude).isEqualTo(entity.longitude)
        assertThat(output.data.detectedPollution).isEqualTo(entity.detectedPollution)
        assertThat(output.data.pollutionObservedByAuthorizedAgent).isEqualTo(entity.pollutionObservedByAuthorizedAgent)
        assertThat(output.data.controlMethod).isEqualTo(entity.controlMethod)
        assertThat(output.data.vesselIdentifier).isEqualTo(entity.vesselIdentifier)
        assertThat(output.data.vesselType).isEqualTo(entity.vesselType)
        assertThat(output.data.vesselSize).isEqualTo(entity.vesselSize)
        assertThat(output.data.identityControlledPerson).isEqualTo(entity.identityControlledPerson)
        assertThat(output.data.nbOfInterceptedVessels).isEqualTo(entity.nbOfInterceptedVessels)
        assertThat(output.data.nbOfInterceptedMigrants).isEqualTo(entity.nbOfInterceptedMigrants)
        assertThat(output.data.nbOfSuspectedSmugglers).isEqualTo(entity.nbOfSuspectedSmugglers)
        assertThat(output.data.isVesselRescue).isEqualTo(entity.isVesselRescue)
        assertThat(output.data.isPersonRescue).isEqualTo(entity.isPersonRescue)
        assertThat(output.data.isVesselNoticed).isEqualTo(entity.isVesselNoticed)
        assertThat(output.data.isVesselTowed).isEqualTo(entity.isVesselTowed)
        assertThat(output.data.isInSRRorFollowedByCROSSMRCC).isEqualTo(entity.isInSRRorFollowedByCROSSMRCC)
        assertThat(output.data.numberPersonsRescued).isEqualTo(entity.numberPersonsRescued)
        assertThat(output.data.numberOfDeaths).isEqualTo(entity.numberOfDeaths)
        assertThat(output.data.operationFollowsDEFREP).isEqualTo(entity.operationFollowsDEFREP)
        assertThat(output.data.locationDescription).isEqualTo(entity.locationDescription)
        assertThat(output.data.isMigrationRescue).isEqualTo(entity.isMigrationRescue)
        assertThat(output.data.nbOfVesselsTrackedWithoutIntervention).isEqualTo(entity.nbOfVesselsTrackedWithoutIntervention)
        assertThat(output.data.nbAssistedVesselsReturningToShore).isEqualTo(entity.nbAssistedVesselsReturningToShore)
        assertThat(output.data.reason).isEqualTo(entity.reason)
    }

    @Nested
    inner class MissionNavActionDataStatusTest {

        @Test
        fun `execute should use input status when is Status Action`() {
            val model = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING)
            val input = MissionAction.fromMissionActionEntity(MissionNavActionEntity.fromMissionActionModel(model))
            val output = MissionNavActionData.toMissionNavActionEntity(input = input!!)

            assertThat(output.status).isEqualTo(input.status)
        }

        @Test
        fun `execute should use action data status when is Status Action and input status is null`() {
            val model = MissionActionModelMock.create(actionType = ActionType.STATUS, status = ActionStatusType.NAVIGATING)
            var input = MissionAction.fromMissionActionEntity(MissionNavActionEntity.fromMissionActionModel(model))
            val output = MissionNavActionData.toMissionNavActionEntity(input = input!!)

            assertThat(output.status.toString()).isEqualTo(model.status)
        }

        @Test
        fun `execute should use action data status otherwise`() {
            val model = MissionActionModelMock.create(actionType = ActionType.CONTROL, status = ActionStatusType.NAVIGATING)
            val input = MissionAction.fromMissionActionEntity(MissionNavActionEntity.fromMissionActionModel(model))
            val output = MissionNavActionData.toMissionNavActionEntity(input = input!!)

            assertThat(output.status.toString()).isEqualTo(model.status)
        }

    }

}
