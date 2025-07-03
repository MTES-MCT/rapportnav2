package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.eq
import org.mockito.Captor
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [DeleteNavAction::class])
@ContextConfiguration(classes = [DeleteNavAction::class])
class DeleteNavActionTest {

    @Captor
    lateinit var deleteTargetCaptor: ArgumentCaptor<UUID>

    @Autowired
    private lateinit var deleteNavAction: DeleteNavAction

    @MockitoBean
    private lateinit var deleteTarget: DeleteTarget

    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @Test
    fun `test execute get nav action by id`() {
        val actionId = UUID.randomUUID()
        val action = MissionActionModel(
            id = actionId,
            missionId = 761,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL
        )

        `when`(missionActionRepository.findById(actionId)).thenReturn(Optional.of(action))
        deleteNavAction = DeleteNavAction(
            deleteTarget = deleteTarget,
            missionActionRepository = missionActionRepository
        )
        deleteNavAction.execute(id = actionId)

        verify(deleteTarget).execute(deleteTargetCaptor.capture(), eq(ActionType.CONTROL))

        assertThat(actionId).isEqualTo(deleteTargetCaptor.value)
        verify(missionActionRepository, times(1)).deleteById(actionId)
    }
}
