package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.MissionActionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action.JPAMissionActionRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAMissionActionRepository::class])
class JPAMissionActionRepositoryTest {

    @MockBean
    private lateinit var dbServiceRepository: IDBMissionActionRepository;

    final val id1 = UUID.randomUUID()
    final val id2 = UUID.randomUUID()
    private val missionActions: List<MissionActionModel> = listOf(
        MissionActionModel(
            id = id1,
            missionId = 761,
            actionType = ActionType.CONTROL.toString(),
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModel(
            id = UUID.randomUUID(),
            missionId = 761,
            actionType = ActionType.ILLEGAL_IMMIGRATION.toString(),
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        ),
        MissionActionModel(
            id = id2,
            missionId = 761,
            actionType = ActionType.SURVEILLANCE.toString(),
            startDateTimeUtc = Instant.parse("2024-04-17T07:00:00Z"),
        )
    );

    @Test
    fun `execute should retrieve action by mission id`() {
        Mockito.`when`(dbServiceRepository.findAllByMissionId(761)).thenReturn(missionActions);
        val jPAMissionActionRepository = JPAMissionActionRepository(dbServiceRepository)
        val responses = jPAMissionActionRepository.findByMissionId(missionId = 761)
        assertThat(responses).isNotNull();
        assertThat(responses.size).isEqualTo(3);
        assertThat(responses.map { action -> action.id }).containsAll(listOf(id1, id2));
    }

    @Test
    fun `execute should retrieve action by id`() {
        Mockito.`when`(dbServiceRepository.findById(id1)).thenReturn(Optional.of(missionActions.get(0)));
        val jPAMissionActionRepository = JPAMissionActionRepository(dbServiceRepository)
        val response = jPAMissionActionRepository.findById(id = id1).orElse(null)
        assertThat(response).isNotNull();
        assertThat(response?.id).isEqualTo(id1);
    }

}
