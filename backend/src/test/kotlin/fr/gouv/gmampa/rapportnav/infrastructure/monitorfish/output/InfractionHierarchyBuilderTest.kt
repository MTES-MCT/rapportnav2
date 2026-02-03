package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.InfractionHierarchyBuilder
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class InfractionHierarchyBuilderTest {

    data class TestInfraction(
        val threat: String,
        val characterization: String,
        val natinfCode: Int,
        val infractionName: String
    )

    @Nested
    inner class BuildThreatHierarchy {

        @Test
        fun `should build hierarchy with single item`() {
            val items = listOf(
                TestInfraction(
                    threat = "Mesures techniques",
                    characterization = "Autorisation",
                    natinfCode = 27718,
                    infractionName = "Infraction description"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals(1, result.size)
            assertEquals("Mesures techniques", result[0].label)
            assertEquals("Mesures techniques", result[0].value)
            assertEquals(1, result[0].children.size)
            assertEquals("Autorisation", result[0].children[0].label)
            assertEquals(1, result[0].children[0].children.size)
            assertEquals("27718 - Infraction description", result[0].children[0].children[0].label)
            assertEquals(27718, result[0].children[0].children[0].value)
        }

        @Test
        fun `should group items by threat`() {
            val items = listOf(
                TestInfraction(
                    threat = "Threat A",
                    characterization = "Char 1",
                    natinfCode = 1001,
                    infractionName = "Infraction 1"
                ),
                TestInfraction(
                    threat = "Threat A",
                    characterization = "Char 2",
                    natinfCode = 1002,
                    infractionName = "Infraction 2"
                ),
                TestInfraction(
                    threat = "Threat B",
                    characterization = "Char 3",
                    natinfCode = 1003,
                    infractionName = "Infraction 3"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals(2, result.size)

            val threatA = result.find { it.label == "Threat A" }
            val threatB = result.find { it.label == "Threat B" }

            assertNotNull(threatA)
            assertNotNull(threatB)
            assertEquals(2, threatA!!.children.size)
            assertEquals(1, threatB!!.children.size)
        }

        @Test
        fun `should group items by characterization within same threat`() {
            val items = listOf(
                TestInfraction(
                    threat = "Threat A",
                    characterization = "Char 1",
                    natinfCode = 1001,
                    infractionName = "Infraction 1"
                ),
                TestInfraction(
                    threat = "Threat A",
                    characterization = "Char 1",
                    natinfCode = 1002,
                    infractionName = "Infraction 2"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals(1, result.size)
            assertEquals(1, result[0].children.size)
            assertEquals(2, result[0].children[0].children.size)
        }

        @Test
        fun `should return empty list for empty input`() {
            val items = emptyList<TestInfraction>()

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertTrue(result.isEmpty())
        }

        @Test
        fun `should format natinf label with code only when name is empty`() {
            val items = listOf(
                TestInfraction(
                    threat = "Threat",
                    characterization = "Char",
                    natinfCode = 12345,
                    infractionName = ""
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals("12345", result[0].children[0].children[0].label)
        }

        @Test
        fun `should format natinf label with code and name when name is provided`() {
            val items = listOf(
                TestInfraction(
                    threat = "Threat",
                    characterization = "Char",
                    natinfCode = 12345,
                    infractionName = "Description"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals("12345 - Description", result[0].children[0].children[0].label)
        }

        @Test
        fun `should preserve characterization label and value`() {
            val items = listOf(
                TestInfraction(
                    threat = "Threat",
                    characterization = "Characterization Name",
                    natinfCode = 1000,
                    infractionName = "Infraction"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals("Characterization Name", result[0].children[0].label)
            assertEquals("Characterization Name", result[0].children[0].value)
        }

        @Test
        fun `should handle multiple natinfs under same characterization`() {
            val items = listOf(
                TestInfraction(
                    threat = "Conservation",
                    characterization = "Fishing",
                    natinfCode = 1001,
                    infractionName = "Infraction A"
                ),
                TestInfraction(
                    threat = "Conservation",
                    characterization = "Fishing",
                    natinfCode = 1002,
                    infractionName = "Infraction B"
                ),
                TestInfraction(
                    threat = "Conservation",
                    characterization = "Fishing",
                    natinfCode = 1003,
                    infractionName = "Infraction C"
                )
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals(1, result.size)
            assertEquals(1, result[0].children.size)
            assertEquals(3, result[0].children[0].children.size)

            val natinfCodes = result[0].children[0].children.map { it.value }
            assertTrue(natinfCodes.contains(1001))
            assertTrue(natinfCodes.contains(1002))
            assertTrue(natinfCodes.contains(1003))
        }

        @Test
        fun `should handle complex hierarchy with multiple levels`() {
            val items = listOf(
                TestInfraction("Threat A", "Char A1", 1001, "Desc 1"),
                TestInfraction("Threat A", "Char A1", 1002, "Desc 2"),
                TestInfraction("Threat A", "Char A2", 1003, "Desc 3"),
                TestInfraction("Threat B", "Char B1", 2001, "Desc 4"),
                TestInfraction("Threat C", "Char C1", 3001, "Desc 5"),
                TestInfraction("Threat C", "Char C2", 3002, "Desc 6")
            )

            val result = InfractionHierarchyBuilder.buildThreatHierarchy(
                items = items,
                threatExtractor = { it.threat },
                characterizationExtractor = { it.characterization },
                natinfCodeExtractor = { it.natinfCode },
                infractionNameExtractor = { it.infractionName }
            )

            assertEquals(3, result.size)

            val threatA = result.find { it.label == "Threat A" }!!
            assertEquals(2, threatA.children.size)

            val charA1 = threatA.children.find { it.label == "Char A1" }!!
            assertEquals(2, charA1.children.size)

            val charA2 = threatA.children.find { it.label == "Char A2" }!!
            assertEquals(1, charA2.children.size)
        }
    }
}