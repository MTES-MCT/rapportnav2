package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ProcessNavAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*

@SpringBootTest(classes = [GetNavActionById::class])
@ContextConfiguration(classes = [GetNavActionById::class])
class GetNavActionByIdTest {

    @Autowired
    private lateinit var getNavActionById: GetNavActionById

    @MockitoBean
    private lateinit var missionActionRepository: INavMissionActionRepository

    @MockitoBean
    private lateinit var processNavAction: ProcessNavAction

    @Test
    fun `test execute with null actionId returns null`() {
        assertThat(getNavActionById.execute(actionId = null)).isNull()
    }

    @Test
    fun `test execute with invalid actionId returns null`() {
        val invalidActionId = "invalid-uuid"
        assertThat(getNavActionById.execute(actionId = invalidActionId)).isNull()
    }

    @Test
    fun `test execute get nav action by id`() {
        val missionId = 761
        val actionId = UUID.randomUUID()
        val action = MissionActionModel(
            id = actionId,
            missionId = missionId,
            startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00"),
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true,
            actionType = ActionType.CONTROL,
        )

        val response = MissionNavActionEntity(
            id = actionId,
            missionId = 761,
            actionType = ActionType.ILLEGAL_IMMIGRATION,
            startDateTimeUtc = Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        )

        `when`(processNavAction.execute(anyOrNull())).thenReturn(response)
        `when`(missionActionRepository.findById(actionId)).thenReturn(Optional.of(action))

        val missionNavAction = getNavActionById.execute(actionId = actionId.toString())

        assertThat(missionNavAction).isNotNull
        assertThat(missionNavAction?.getActionId()).isEqualTo(actionId.toString())
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val actionId = UUID.randomUUID()
        `when`(missionActionRepository.findById(actionId)).thenThrow(RuntimeException("Database error"))

        val exception = assertThrows<BackendInternalException> {
            getNavActionById.execute(actionId = actionId.toString())
        }
        assertThat(exception.message).contains("GetNavActionById failed for actionId=$actionId")
    }
}
