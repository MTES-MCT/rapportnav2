package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MissionGeneralInfoEntity2Tests {

        @Nested
        inner class IsCompleteForStatsTests {

            @Test
            fun `should return true when crew is not empty and correct general info`() {
                // Given
                val crew = listOf(MissionCrewEntityMock.create())
                val data = createCompleteData()
                val entity = MissionGeneralInfoEntity2(data = data, crew = crew)

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertTrue(result)
            }

            @Test
            fun `should return false when data is null`() {
                // Given
                val entity = MissionGeneralInfoEntity2(data = null, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when consumedFuelInLiters is null`() {
                // Given
                val data = createCompleteData().copy(consumedFuelInLiters = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when consumedGOInLiters is null`() {
                // Given
                val data = createCompleteData().copy(consumedGOInLiters = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when distanceInNauticalMiles is null`() {
                // Given
                val data = createCompleteData().copy(distanceInNauticalMiles = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when crew is empty and data is complete`() {
                // Given
                val data = createCompleteData()
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when crew is null and data is complete`() {
                // Given
                val data = createCompleteData()
                val entity = MissionGeneralInfoEntity2(data = data, crew = null)

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }
        }

        // Helper method to create a complete MissionGeneralInfoEntity
        private fun createCompleteData(): MissionGeneralInfoEntity {
            return MissionGeneralInfoEntity(
                id = 1,
                missionId = 1,
                consumedFuelInLiters = 100f,
                consumedGOInLiters = 50f,
                distanceInNauticalMiles = 200f
            )
        }
}
