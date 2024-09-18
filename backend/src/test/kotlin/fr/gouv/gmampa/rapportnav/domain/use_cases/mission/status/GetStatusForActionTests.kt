package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.status

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionStatusModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import java.time.Instant
import java.util.*


@SpringBootTest(classes = [GetStatusForAction::class])
class GetStatusForActionTests {

    companion object {
        @Container
        val container: PostgreSQLContainer<*> = PostgreSQLContainer<Nothing>("postgres:latest")
            .apply {
                withDatabaseName("rapportnavdb")
                withUsername("postgres")
                withPassword("postgres")
            }

        init {
            container.start()
        }
    }

    private var missionId: Int = 1

    @MockBean
    private lateinit var statusActionsRepository: INavActionStatusRepository

    @Autowired
    private lateinit var getStatusForAction: GetStatusForAction

    @Test
    fun `execute Should return Unknown when action is empty list for a mission`() {
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(listOf());
        val statusForAction = getStatusForAction.execute(missionId = missionId, actionStartDateTimeUtc = null);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNKNOWN);
    }

    @Test
    fun `execute Should return the last action status if the last action is a starting status`() {
        val startDatetime = Instant.parse("2022-01-01T11:00:00Z")
        val startingAction = ActionStatusEntity(
            id = UUID.randomUUID(),
            missionId = missionId,
            startDateTimeUtc = startDatetime,
            status = ActionStatusType.UNAVAILABLE,
        )
        val actions = listOf(startingAction)
        given(this.statusActionsRepository.findAllByMissionId(missionId = 1)).willReturn(actions.map {
            ActionStatusModel.fromActionStatusEntity(
                it
            )
        });
        val statusForAction = getStatusForAction.execute(missionId = missionId, actionStartDateTimeUtc = startDatetime);
        assertThat(statusForAction).isEqualTo(ActionStatusType.UNAVAILABLE);
    }
}
