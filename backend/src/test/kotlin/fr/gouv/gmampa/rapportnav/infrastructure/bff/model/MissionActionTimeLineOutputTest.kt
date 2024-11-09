package fr.gouv.gmampa.rapportnav.infrastructure.bff.model

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionActionTimeLineOutput
import fr.gouv.gmampa.rapportnav.mocks.mission.action.EnvActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionActionModelMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionActionTimeLineOutputTest {

    @Test
    fun `execute should retrieve output from mission action nav Entity`() {
        val model = MissionActionModelMock.create()
        val entity = MissionNavActionEntity.fromMissionActionModel(model)
        val timelineOutput = MissionActionTimeLineOutput.fromMissionActionEntity(entity)
        assertThat(timelineOutput).isNotNull()
        assertThat(timelineOutput?.id).isEqualTo(entity.id.toString())
        assertThat(timelineOutput?.missionId).isEqualTo(entity.missionId)
        assertThat(timelineOutput?.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(timelineOutput?.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(timelineOutput?.observations).isEqualTo(entity.observations)
        assertThat(timelineOutput?.controlMethod).isEqualTo(entity.controlMethod)
        assertThat(timelineOutput?.vesselIdentifier).isEqualTo(entity.vesselIdentifier)
        assertThat(timelineOutput?.vesselType).isEqualTo(entity.vesselType)
        assertThat(timelineOutput?.vesselSize).isEqualTo(entity.vesselSize)
        assertThat(timelineOutput?.isVesselRescue).isEqualTo(entity.isVesselRescue)
        assertThat(timelineOutput?.isPersonRescue).isEqualTo(entity.isPersonRescue)
        assertThat(timelineOutput?.reason).isEqualTo(entity.reason)
    }

    @Test
    fun `execute should retrieve timeline out put from entity`() {
        val envAction = EnvActionControlMock.create(
            UUID.randomUUID(),
            vehicleType = VehicleTypeEnum.VEHICLE_AIR,
            observations = "observations",
            completion = ActionCompletionEnum.TO_COMPLETE,
            actionNumberOfControls = 3,
            actionTargetType = ActionTargetTypeEnum.COMPANY,
            actionStartDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            actionEndDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        )
        val entity = MissionEnvActionEntity.fromEnvAction(missionId = 761, action = envAction)
        val timelineOutput = MissionActionTimeLineOutput.fromMissionActionEntity(entity)

        assertThat(timelineOutput).isNotNull()
        assertThat(timelineOutput?.id).isEqualTo(entity.id.toString())
        assertThat(timelineOutput?.missionId).isEqualTo(entity.missionId)
        assertThat(timelineOutput?.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(timelineOutput?.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(timelineOutput?.observations).isEqualTo(entity.observations)
        assertThat(timelineOutput?.vehicleType).isEqualTo(envAction.vehicleType)
        assertThat(timelineOutput?.actionNumberOfControls).isEqualTo(envAction.actionNumberOfControls)
        assertThat(timelineOutput?.actionTargetType).isEqualTo(envAction.actionTargetType)
    }


    @Test
    fun `execute should retrieve retrieve timeline out put from entity`() {
        val fishAction = FishActionControlMock.create()
        val entity = MissionFishActionEntity.fromFishAction(action = fishAction)
        val timelineOutput = MissionActionTimeLineOutput.fromMissionActionEntity(entity)
        assertThat(timelineOutput).isNotNull()
        assertThat(timelineOutput?.id).isEqualTo(entity.id.toString())
        assertThat(timelineOutput?.startDateTimeUtc).isEqualTo(entity.actionDatetimeUtc)
        assertThat(timelineOutput?.endDateTimeUtc).isEqualTo(entity.actionEndDatetimeUtc)
        assertThat(timelineOutput?.vesselId).isEqualTo(entity.vesselId)
        assertThat(timelineOutput?.vesselName).isEqualTo(entity.vesselName)
    }
}
