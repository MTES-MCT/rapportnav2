package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionActionControls
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionEnvActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionFishActionEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.action.MissionNavActionEntityMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [GetMissionActionControls::class])
class GetMissionActionControlsTest {

    @Autowired
    private lateinit var getMissionActionControls: GetMissionActionControls

    @Test
    fun `execute should return emptyList if mission is null`() {
        val actual = getMissionActionControls.execute(null)
        val expected = emptyList<MissionActionEntity>()
        assertEquals(actual, expected)
    }

    @Test
    fun `execute should return list of combined actions`() {

        val navControl1 = MissionNavActionEntityMock.create(actionType = ActionType.CONTROL)
        val navActions = listOf<MissionNavActionEntity>(
            navControl1,
            MissionNavActionEntityMock.create(actionType = ActionType.STATUS),
        )
        val envControl1 = MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.CONTROL)
        val envActions = listOf<MissionEnvActionEntity>(
            envControl1,
            MissionEnvActionEntityMock.create(envActionType = ActionTypeEnum.SURVEILLANCE),
        )
        val fishControl1 = MissionFishActionEntityMock.create(fishActionType = MissionActionType.LAND_CONTROL)
        val fishControl2 = MissionFishActionEntityMock.create(fishActionType = MissionActionType.SEA_CONTROL)
        val fishActions = listOf<MissionFishActionEntity>(
            fishControl1,
            fishControl2,
            MissionFishActionEntityMock.create(fishActionType = MissionActionType.AIR_CONTROL),
            MissionFishActionEntityMock.create(fishActionType = MissionActionType.AIR_SURVEILLANCE),
        )
        val mockMission = MissionEntityMock.create(actions = navActions + envActions + fishActions)

        val actual = getMissionActionControls.execute(mockMission)
        val expected = listOf<MissionActionEntity>(
            navControl1, fishControl1, fishControl2, envControl1
        )

        assertEquals(actual, expected)
    }
}
