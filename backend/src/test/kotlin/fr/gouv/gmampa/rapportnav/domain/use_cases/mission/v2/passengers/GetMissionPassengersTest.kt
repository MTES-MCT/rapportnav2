package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.GetMissionPassengers
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetMissionPassengers::class])
class GetMissionPassengersTest {
    @Autowired
    private lateinit var useCase: GetMissionPassengers

    @MockitoBean
    private lateinit var repo: IMissionPassengerRepository

    val passenger1 = MissionPassengerEntityMock.create(fullName = "John Doe")
    val passenger2 = MissionPassengerEntityMock.create(fullName = "Jane Smith")

    @Test
    fun `executeById throws if missionId is null`() {
        // When - Then
        assertThrows<IllegalArgumentException> {
            useCase.execute(missionId = null)
        }
        verify(repo, never()).findByMissionId(any())
    }

    @Test
    fun `executeById returns mapped passengers`() {
        val missionId = 42

        whenever(repo.findByMissionId(missionId))
            .thenReturn(listOf(passenger1.toMissionPassengerModel(), passenger2.toMissionPassengerModel()))

        val result = useCase.execute(missionId = missionId)

        assertThat(result)
            .hasSize(2)
            .extracting<String> { it.fullName }
            .containsExactly("John Doe", "Jane Smith")

        verify(repo, times(1)).findByMissionId(missionId)
    }

    @Test
    fun `executeByUUID throws if uuid is null`() {
        assertThrows<IllegalArgumentException> {
            useCase.execute(missionIdUUID = null)
        }
        verify(repo, never()).findByMissionIdUUID(any())
    }

    @Test
    fun `executeByUUID returns mapped passengers`() {
        val uuid = UUID.randomUUID()

        whenever(repo.findByMissionIdUUID(uuid))
            .thenReturn(listOf(passenger1.toMissionPassengerModel()))

        val result = useCase.execute(missionIdUUID = uuid)

        assertThat(result).hasSize(1)
        assertThat(result.first().fullName).isEqualTo("John Doe")

        verify(repo, times(1)).findByMissionIdUUID(uuid)
    }


}
