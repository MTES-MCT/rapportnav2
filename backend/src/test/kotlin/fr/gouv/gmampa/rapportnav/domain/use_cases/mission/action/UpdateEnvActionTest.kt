package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfractionEnvTarget
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionEnvActionDataInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [UpdateEnvAction::class])
@ContextConfiguration(classes = [UpdateEnvAction::class])
class UpdateEnvActionTest {

    @MockBean
    private lateinit var patchEnvAction: PatchEnvAction

    @MockBean
    private lateinit var processMissionActionControl: ProcessMissionActionControl

    @MockBean
    private lateinit var processMissionActionInfractionEnvTarget: ProcessMissionActionInfractionEnvTarget


    @Test
    fun `test execute update env action`() {
        val actionId =  UUID.randomUUID().toString()
        val input = MissionActionInput(
            id = actionId,
            missionId = 761,
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORTNAV,
            env = missionEnvActionDataInput(),
        )

        `when`(patchEnvAction.execute(anyOrNull())).thenReturn(null)
        `when`(processMissionActionControl.execute(anyOrNull())).thenReturn(ActionControlEntity())
        `when`(processMissionActionInfractionEnvTarget.execute(actionId, listOf())).thenReturn(listOf())

        val updateNavAction = UpdateEnvAction(
            patchEnvAction = patchEnvAction,
            processMissionActionControl = processMissionActionControl,
            processMissionActionInfractionEnvTarget = processMissionActionInfractionEnvTarget
        )

        val response = updateNavAction.execute(input)
        assertThat(response).isNotNull

    }

    private fun missionEnvActionDataInput() = MissionEnvActionDataInput(
        observationsByUnit= "MyObservations",
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
        infractions = listOf()
    )
}
