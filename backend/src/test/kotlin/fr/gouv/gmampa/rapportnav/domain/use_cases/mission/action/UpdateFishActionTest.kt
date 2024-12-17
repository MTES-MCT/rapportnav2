package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.UpdateFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2.ProcessMissionActionControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2.ProcessMissionActionInfraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionActionInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.MissionFishActionDataInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import java.time.Instant

@SpringBootTest(classes = [UpdateFishAction::class])
@ContextConfiguration(classes = [UpdateFishAction::class])
class UpdateFishActionTest {

    @MockBean
    private lateinit var patchFishAction: PatchFishAction

    @MockBean
    private lateinit var processMissionActionControl: ProcessMissionActionControl

    @MockBean
    private lateinit var processMissionActionInfraction: ProcessMissionActionInfraction


    @Test
    fun `test execute update fish action`() {
        val actionId =  54566.toString()
        val input = MissionActionInput(
            id = actionId,
            missionId = 761,
            actionType = ActionType.CONTROL,
            source = MissionSourceEnum.RAPPORTNAV,
            fish = missionFishActionDataInput(),
        )

        `when`(patchFishAction.execute(anyOrNull())).thenReturn(null)
        `when`(processMissionActionControl.execute(anyOrNull())).thenReturn(ActionControlEntity())
        `when`(processMissionActionInfraction.execute(actionId, ActionControlEntity())).thenReturn(listOf())

        val updateNavAction = UpdateFishAction(
            patchFishAction = patchFishAction,
            processMissionActionControl = processMissionActionControl,
            processMissionActionInfraction = processMissionActionInfraction
        )

        val response = updateNavAction.execute(input)
        assertThat(response).isNotNull

    }

    private fun missionFishActionDataInput() = MissionFishActionDataInput(
        observations = "MyObservations",
        startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
    )
}
