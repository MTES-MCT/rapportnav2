package fr.gouv.gmampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBServiceRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew.JPAServiceRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [JPAServiceRepository::class])
class JPAServiceRepositoryTest {


    @MockBean
    private lateinit var dbServiceRepository: IDBServiceRepository;


    private val serviceModels: List<ServiceModel> = listOf(
        ServiceModel(
            id = 3,
            name = "firstService",
            controlUnits = listOf(1, 3)
        ), ServiceModel(
            id = 4,
            name = "SecondService",
            controlUnits = listOf(3, 4)
        ),
        ServiceModel(
            id = 3,
            name = "thirdService",
            controlUnits = listOf(4, 5)
        )
    );


    @Test
    fun `execute should retrieve serviceModel with control units list`() {
        Mockito.`when`(dbServiceRepository.findAll()).thenReturn(serviceModels);
        val jpaServiceRepo = JPAServiceRepository(dbServiceRepository)
        val response = jpaServiceRepo.findByControlUnitId(listOf(3))
        assertThat(response).isNotNull();

        assertThat(response.size).isEqualTo(2);
        assertThat(response.map { service -> service.id }).containsAll(listOf(3, 4));
    }

    @Test
    fun `execute should retrieve serviceModel with control units list when receiving several controlUnits in input`() {
        Mockito.`when`(dbServiceRepository.findAll()).thenReturn(serviceModels);
        val jpaServiceRepo = JPAServiceRepository(dbServiceRepository)
        val response = jpaServiceRepo.findByControlUnitId(listOf(3, 348))
        assertThat(response).isNotNull();

        assertThat(response.size).isEqualTo(2);
        assertThat(response.map { service -> service.id }).containsAll(listOf(3, 4));
    }

}
