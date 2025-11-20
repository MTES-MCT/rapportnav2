package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData
import fr.gouv.gmampa.rapportnav.mocks.mission.TargetEntity2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [UpdateEnvAction::class])
@ContextConfiguration(classes = [UpdateEnvAction::class])
class UpdateEnvActionTest {

    @MockitoBean
    private lateinit var patchEnvAction: PatchEnvAction

    @MockitoBean
    private lateinit var  processMissionActionTarget: ProcessMissionActionTarget


    @Test
    fun `test execute update env action`() {
        val actionId = UUID.randomUUID().toString()
        val input = MissionEnvAction(
            id = actionId,
            missionId = 761,
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORTNAV,
            data = getEnvActionData(),
        )

        `when`(patchEnvAction.execute(anyOrNull())).thenReturn(null)
        `when`(
            processMissionActionTarget.execute(
                anyOrNull(),
                anyOrNull()
            )
        ).thenReturn(listOf(TargetEntity2Mock.create()))

        val updateEnvAction = UpdateEnvAction(
            patchEnvAction = patchEnvAction,
            processMissionActionTarget = processMissionActionTarget
        )

        val response = updateEnvAction.execute(actionId, input)
        assertThat(response).isNotNull

    }

    private fun getEnvActionData() = MissionEnvActionData(
        observations = "MyObservations",
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
    )
}
