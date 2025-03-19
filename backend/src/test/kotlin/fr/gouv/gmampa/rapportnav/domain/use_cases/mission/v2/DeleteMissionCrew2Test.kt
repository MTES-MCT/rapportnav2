package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.DeleteMissionCrew2
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`

@SpringBootTest(classes = [DeleteMissionCrew2::class])
class DeleteMissionCrew2Test {

    @Autowired
    private lateinit var deleteMissionCrew2: DeleteMissionCrew2

    @MockitoBean
    private lateinit var crewRepository: IMissionCrewRepository

    @Test
    fun `should return true when crew is successfully deleted`() {
        // Given
        val crewId = 1
        `when`(crewRepository.deleteById(crewId)).thenReturn(true)

        // When
        val result = deleteMissionCrew2.execute(crewId)

        // Then
        assertTrue(result)
    }

    @Test
    fun `should return false when crew is not found`() {
        // Given
        val crewId = 999
        `when`(crewRepository.deleteById(crewId)).thenThrow(NoSuchElementException("Crew not found"))

        // When
        val result = deleteMissionCrew2.execute(crewId)

        // Then
        assertFalse(result)
    }

    @Test
    fun `should return false for any other exception`() {
        // Given
        val crewId = 1
        `when`(crewRepository.deleteById(crewId)).thenThrow(RuntimeException("Database error"))

        // When
        val result = deleteMissionCrew2.execute(crewId)

        // Then
        assertFalse(result)
    }

}
