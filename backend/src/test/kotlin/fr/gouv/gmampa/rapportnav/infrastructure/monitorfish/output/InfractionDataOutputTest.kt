package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionCategory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output.InfractionDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class InfractionDataOutputTest {

    @Test
    fun `should map all fields to NatinfEntity`() {
        val output = InfractionDataOutput(
            natinfCode = 27718,
            regulation = "Fishing Regulation",
            infractionCategory = "FISHING",
            infraction = "Illegal landing",
        )

        assertEquals(
            NatinfEntity(
                natinfCode = 27718,
                infraction = "Illegal landing",
                regulation = "Fishing Regulation",
                infractionCategory = "Pêche",
            ),
            output.toNatinfEntity()
        )
    }

    @Test
    fun `should keep optional fields null`() {
        val output = InfractionDataOutput(natinfCode = 27718, infraction = "Illegal landing")

        assertEquals(NatinfEntity(natinfCode = 27718, infraction = "Illegal landing"), output.toNatinfEntity())
    }

    @ParameterizedTest
    @EnumSource(InfractionCategory::class)
    fun `should map infraction category name to its label`(category: InfractionCategory) {
        val output = InfractionDataOutput(natinfCode = 1, infraction = "x", infractionCategory = category.name)

        assertEquals(category.value, output.toNatinfEntity()?.infractionCategory)
    }

    @Test
    fun `should pass through unknown infraction category`() {
        val output = InfractionDataOutput(natinfCode = 1, infraction = "x", infractionCategory = "NEW_CATEGORY")

        assertEquals("NEW_CATEGORY", output.toNatinfEntity()?.infractionCategory)
    }

    @Test
    fun `should map literal null infraction category to null`() {
        val output = InfractionDataOutput(natinfCode = 1, infraction = "x", infractionCategory = "null")

        assertNull(output.toNatinfEntity()?.infractionCategory)
    }

    @Test
    fun `should return null when natinf code is missing`() {
        assertNull(InfractionDataOutput(infraction = "Illegal landing").toNatinfEntity())
    }

    @Test
    fun `should return null when infraction label is missing`() {
        assertNull(InfractionDataOutput(natinfCode = 27718).toNatinfEntity())
    }
}
