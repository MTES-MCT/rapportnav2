package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.Infraction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionThreatCharacterization
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.InfractionThreatCharacterizationDataOutput
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InfractionThreatCharacterizationDataOutputTest {

    @Nested
    inner class FromInfractionThreatCharacterization {

        @Test
        fun `should build hierarchy from list of InfractionThreatCharacterization`() {
            val infractions = listOf(
                InfractionThreatCharacterization(
                    natinfCode = 27718,
                    infraction = "Illegal landing",
                    threat = "Conservation",
                    threatCharacterization = "Landing"
                ),
                InfractionThreatCharacterization(
                    natinfCode = 27719,
                    infraction = "Illegal gear",
                    threat = "Conservation",
                    threatCharacterization = "Gear"
                )
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            assertEquals(1, result.size)
            assertEquals("Conservation", result[0].label)
            assertEquals(2, result[0].children.size)
        }

        @Test
        fun `should group by threat correctly`() {
            val infractions = listOf(
                InfractionThreatCharacterization(
                    natinfCode = 1001,
                    infraction = "Infraction 1",
                    threat = "Threat A",
                    threatCharacterization = "Char A"
                ),
                InfractionThreatCharacterization(
                    natinfCode = 1002,
                    infraction = "Infraction 2",
                    threat = "Threat B",
                    threatCharacterization = "Char B"
                ),
                InfractionThreatCharacterization(
                    natinfCode = 1003,
                    infraction = "Infraction 3",
                    threat = "Threat A",
                    threatCharacterization = "Char A2"
                )
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            assertEquals(2, result.size)

            val threatA = result.find { it.label == "Threat A" }
            val threatB = result.find { it.label == "Threat B" }

            assertNotNull(threatA)
            assertNotNull(threatB)
            assertEquals(2, threatA!!.children.size)
            assertEquals(1, threatB!!.children.size)
        }

        @Test
        fun `should return empty list for empty input`() {
            val infractions = emptyList<InfractionThreatCharacterization>()

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            assertTrue(result.isEmpty())
        }

        @Test
        fun `should include natinf code in label`() {
            val infractions = listOf(
                InfractionThreatCharacterization(
                    natinfCode = 27718,
                    infraction = "Illegal landing",
                    threat = "Conservation",
                    threatCharacterization = "Landing"
                )
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            val natinfOutput = result[0].children[0].children[0]
            assertEquals("27718 - Illegal landing", natinfOutput.label)
            assertEquals(27718, natinfOutput.value)
        }

        @Test
        fun `should handle single infraction`() {
            val infractions = listOf(
                InfractionThreatCharacterization(
                    natinfCode = 12345,
                    infraction = "Test infraction",
                    threat = "Test threat",
                    threatCharacterization = "Test characterization"
                )
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            assertEquals(1, result.size)
            assertEquals("Test threat", result[0].label)
            assertEquals("Test threat", result[0].value)
            assertEquals(1, result[0].children.size)
            assertEquals("Test characterization", result[0].children[0].label)
            assertEquals(1, result[0].children[0].children.size)
            assertEquals(12345, result[0].children[0].children[0].value)
        }

        @Test
        fun `should group multiple natinfs under same characterization`() {
            val infractions = listOf(
                InfractionThreatCharacterization(
                    natinfCode = 1001,
                    infraction = "Infraction 1",
                    threat = "Threat",
                    threatCharacterization = "Char"
                ),
                InfractionThreatCharacterization(
                    natinfCode = 1002,
                    infraction = "Infraction 2",
                    threat = "Threat",
                    threatCharacterization = "Char"
                )
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfractionThreatCharacterization(infractions)

            assertEquals(1, result.size)
            assertEquals(1, result[0].children.size)
            assertEquals(2, result[0].children[0].children.size)
        }
    }

    @Nested
    inner class FromInfraction {

        @Test
        fun `should create single threat hierarchy from Infraction`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 27718,
                threat = "Conservation",
                threatCharacterization = "Landing"
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

            assertEquals("Conservation", result.label)
            assertEquals("Conservation", result.value)
            assertEquals(1, result.children.size)
            assertEquals("Landing", result.children[0].label)
        }

        @Test
        fun `should use fallback for null threat`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 12345,
                threat = null,
                threatCharacterization = "Some characterization"
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

            assertEquals("Famille inconnue", result.label)
            assertEquals("Famille inconnue", result.value)
        }

        @Test
        fun `should use fallback for null characterization`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 12345,
                threat = "Some threat",
                threatCharacterization = null
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

            assertEquals("Type inconnu", result.children[0].label)
            assertEquals("Type inconnu", result.children[0].value)
        }

        @Test
        fun `should use fallback for both null threat and characterization`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITHOUT_RECORD,
                natinf = 99999,
                threat = null,
                threatCharacterization = null
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

            assertEquals("Famille inconnue", result.label)
            assertEquals("Type inconnu", result.children[0].label)
        }

        @Test
        fun `should include natinf code only in label when no infraction name`() {
            val infraction = Infraction(
                infractionType = InfractionType.WITH_RECORD,
                natinf = 27718,
                threat = "Threat",
                threatCharacterization = "Char"
            )

            val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

            val natinfOutput = result.children[0].children[0]
            assertEquals("27718", natinfOutput.label)
            assertEquals(27718, natinfOutput.value)
        }

        @Test
        fun `should handle different infraction types`() {
            val infractionTypes = listOf(
                InfractionType.WITH_RECORD,
                InfractionType.WITHOUT_RECORD,
                InfractionType.PENDING
            )

            infractionTypes.forEach { type ->
                val infraction = Infraction(
                    infractionType = type,
                    natinf = 12345,
                    threat = "Threat",
                    threatCharacterization = "Char"
                )

                val result = InfractionThreatCharacterizationDataOutput.fromInfraction(infraction)

                assertNotNull(result)
                assertEquals("Threat", result.label)
            }
        }
    }
}