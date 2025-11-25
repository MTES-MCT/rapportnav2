package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.passengers

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.DeleteMissionPassenger
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@SpringBootTest(classes = [DeleteMissionPassenger::class])
class DeleteMissionPassengerTest {

    @Autowired
    private lateinit var useCase: DeleteMissionPassenger

    @MockitoBean
    private lateinit var crewRepository: IMissionPassengerRepository

    @Test
    fun `should return true when crew is successfully deleted`() {
        // Given
        val crewId = 1
        `when`(crewRepository.deleteById(crewId)).thenReturn(true)

        // When
        val result = useCase.execute(crewId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `should return false when crew is not found`() {
        // Given
        val crewId = 999
        `when`(crewRepository.deleteById(crewId)).thenThrow(NoSuchElementException("Crew not found"))

        // When
        val result = useCase.execute(crewId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `should return false for any other exception`() {
        // Given
        val crewId = 1
        `when`(crewRepository.deleteById(crewId)).thenThrow(RuntimeException("Database error"))

        // When
        val result = useCase.execute(crewId)

        // Then
        assertFalse(result)
    }

}
