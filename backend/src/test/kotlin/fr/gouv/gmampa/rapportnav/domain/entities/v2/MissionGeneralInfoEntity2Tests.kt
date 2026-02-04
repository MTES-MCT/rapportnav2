package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.InterMinisterialServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.MissionCrewEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MissionGeneralInfoEntity2Tests {

        @Nested
        inner class IsCompleteForStatsPamTests {

            @Test
            fun `should return true when crew is not empty and correct general info`() {
                // Given
                val crew = listOf(MissionCrewEntityMock.create())
                val data = createCompleteDataPam()
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
                val data = createCompleteDataPam().copy(consumedFuelInLiters = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when consumedGOInLiters is null`() {
                // Given
                val data = createCompleteDataPam().copy(consumedGOInLiters = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when distanceInNauticalMiles is null`() {
                // Given
                val data = createCompleteDataPam().copy(distanceInNauticalMiles = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when nbrOfRecognizedVessel is null`() {
                // Given
                val data = createCompleteDataPam().copy(nbrOfRecognizedVessel = null)
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when crew is empty and data is complete`() {
                // Given
                val data = createCompleteDataPam()
                val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }

            @Test
            fun `should return false when crew is null and data is complete`() {
                // Given
                val data = createCompleteDataPam()
                val entity = MissionGeneralInfoEntity2(data = data, crew = null)

                // When
                val result = entity.isCompleteForStats()

                // Then
                assertFalse(result)
            }
        }

        // Helper method to create a complete MissionGeneralInfoEntity
        private fun createCompleteDataPam(): MissionGeneralInfoEntity {
            return MissionGeneralInfoEntity(
                id = 1,
                missionId = 1,
                consumedFuelInLiters = 100f,
                consumedGOInLiters = 50f,
                distanceInNauticalMiles = 200f,
                nbrOfRecognizedVessel = 2,
                service = ServiceEntityMock.create(serviceType = ServiceTypeEnum.PAM),
            )
        }

    @Nested
    inner class IsCompleteForStatsUlamTests {


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
        fun `should return false when resources is empty and isResourceNotUsed is false `() {
            // Given
            val data = createCompleteDataUlam()
            val crew = listOf(MissionCrewEntityMock.create())

            data.resources = emptyList()
            data.isResourcesNotUsed = false

            val entity = MissionGeneralInfoEntity2(
                data = data,
                crew = crew
            )

            // When
            val result = entity.isResourceComplete()

            // Then
            assertFalse(result)
        }

        @Test
        fun `should return true when data is resources is not empty`() {
            // Given
            val data = createCompleteDataUlam()
            val crew = listOf(MissionCrewEntityMock.create())
            data.resources = listOf(LegacyControlUnitResourceEntity(controlUnitId = 1, id = 2))
            val entity = MissionGeneralInfoEntity2(
                data = data, crew = crew
            )

            // When
            val result = entity.isCompleteForStats()

            // Then
            assertTrue(result)
        }

        @Test
        fun `should return true when resources is empty but isResourceNotUsed true`() {
            // Given
            val data = createCompleteDataUlam()
            data.resources = listOf()
            data.isResourcesNotUsed = true
            val crew = listOf(MissionCrewEntityMock.create())
            val entity = MissionGeneralInfoEntity2(data = data, crew = crew)

            // When
            val result = entity.isCompleteForStats()

            // Then
            assertTrue(result)
        }

        @Test
        fun `should return false when data is resources is not empty but interministerial is checked`() {
            // Given
            val data = createCompleteDataUlam()
            val crew = listOf(MissionCrewEntityMock.create())
            data.resources = listOf(LegacyControlUnitResourceEntity(controlUnitId = 1, id = 2))
            data.isWithInterMinisterialService = true

            val entity = MissionGeneralInfoEntity2(
                data = data, crew = crew
            )

            // When
            val result = entity.isCompleteForStats()

            // Then
            assertFalse(result)
        }


        @Test
        fun `should return true when data is resources is not empty, interministerial is checked and has values`() {
            // Given
            val data = createCompleteDataUlam()
            val crew = listOf(MissionCrewEntityMock.create())
            data.resources = listOf(LegacyControlUnitResourceEntity(controlUnitId = 1, id = 2))
            data.isWithInterMinisterialService = true
            data.interMinisterialServices = listOf(
                InterMinisterialServiceEntity(
                    id = 1,
                    controlUnitId = 2,
                    administrationId = 2
                )
            )
            val entity = MissionGeneralInfoEntity2(
                data = data, crew = crew
            )

            // When
            val result = entity.isCompleteForStats()

            // Then
            assertTrue(result)
        }

    }

    @Test
    fun `should return false when crew is empty and correct general info`() {
        // Given
        val data = createCompleteDataUlam()
        data.resources = listOf(LegacyControlUnitResourceEntity(controlUnitId = 1, id = 2))
        data.isWithInterMinisterialService = true
        data.interMinisterialServices = listOf(
            InterMinisterialServiceEntity(
                id = 1,
                controlUnitId = 2,
                administrationId = 2
            )
        )
        val entity = MissionGeneralInfoEntity2(data = data, crew = emptyList())

        // When
        val result = entity.isCompleteForStats()

        // Then
        assertFalse(result)
    }


    // Helper method to create a complete MissionGeneralInfoEntity
    private fun createCompleteDataUlam(): MissionGeneralInfoEntity {
        return MissionGeneralInfoEntity(
            id = 1,
            missionId = 1,
            service = ServiceEntityMock.create(serviceType = ServiceTypeEnum.ULAM),
        )
    }
}
