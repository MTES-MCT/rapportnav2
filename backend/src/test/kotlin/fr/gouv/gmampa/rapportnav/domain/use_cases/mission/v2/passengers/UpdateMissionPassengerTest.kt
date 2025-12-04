package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.UpdateMissionPassenger
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateAgent2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentInput2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.AgentRoleModel
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*


@SpringBootTest(classes = [UpdateMissionPassenger::class])
class UpdateMissionPassengerTest {

    @MockitoBean
    private lateinit var repo: IMissionPassengerRepository

    @Autowired
    private lateinit var useCase: UpdateMissionPassenger

    @Test
    fun `should create a new passenger`() {
        val passenger = MissionPassengerEntityMock.create()
        Mockito.`when`(repo.save(any())).thenReturn(passenger.toMissionPassengerModel())

        useCase = UpdateMissionPassenger(
            repo = repo,
        )

        val response = useCase.execute(passenger = passenger)
        assertThat(response).isNotNull()
    }

}
