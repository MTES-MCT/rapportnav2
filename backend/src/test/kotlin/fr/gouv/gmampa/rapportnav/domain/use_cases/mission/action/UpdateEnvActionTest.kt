package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControlEnvTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfractionEnvTarget
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionActionTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.ActionControl
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnvActionData
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
    private lateinit var processMissionActionControl: ProcessMissionActionControl

    @MockitoBean
    lateinit var processMissionActionControlEnvTarget: ProcessMissionActionControlEnvTarget

    @MockitoBean
    private lateinit var processMissionActionInfractionEnvTarget: ProcessMissionActionInfractionEnvTarget

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
        `when`(processMissionActionControl.execute(anyOrNull(), anyOrNull())).thenReturn(ActionControl())
        `when`(processMissionActionInfractionEnvTarget.execute(actionId, listOf())).thenReturn(listOf())

        val updateNavAction = UpdateEnvAction(
            patchEnvAction = patchEnvAction,
            processMissionActionControl = processMissionActionControl,
            processMissionActionControlEnvTarget = processMissionActionControlEnvTarget,
            processMissionActionInfractionEnvTarget = processMissionActionInfractionEnvTarget,
            processMissionActionTarget = processMissionActionTarget
        )

        val response = updateNavAction.execute(actionId, input)
        assertThat(response).isNotNull

    }

    private fun getEnvActionData() = MissionEnvActionData(
        observations = "MyObservations",
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        infractions = listOf()
    )
}
