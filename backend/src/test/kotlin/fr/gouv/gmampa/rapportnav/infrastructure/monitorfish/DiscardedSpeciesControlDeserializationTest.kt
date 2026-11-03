package fr.gouv.gmampa.rapportnav.infrastructure.monitorfish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.DiscardedSpeciesControl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import tools.jackson.databind.DeserializationFeature
import tools.jackson.databind.json.JsonMapper
import tools.jackson.module.kotlin.KotlinModule
import tools.jackson.module.kotlin.readValue

class DiscardedSpeciesControlDeserializationTest {

    // as configured in JacksonConfig
    private val mapper = JsonMapper.builder()
        .addModule(KotlinModule.Builder().build())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .build()

    @Test
    fun `should read the species name sent by the MonitorFish public API`() {
        val json = """
            {"speciesCode": "HKE", "speciesName": "MERLU EUROPÉEN", "rejectedWeight": 5.0, "discardReason": "DIM",
             "discardReasonName": "de minimis", "faoZones": ["27.8.a"]}
        """

        val discard = mapper.readValue<DiscardedSpeciesControl>(json)

        assertThat(discard.speciesCode).isEqualTo("HKE")
        assertThat(discard.speciesName).isEqualTo("MERLU EUROPÉEN")
        assertThat(discard.discardReason).isEqualTo(DiscardReason.DIM)
    }

    @Test
    fun `should accept a discard without species name`() {
        val discard = mapper.readValue<DiscardedSpeciesControl>("""{"speciesCode": "COD", "discardReason": "RET"}""")

        assertThat(discard.speciesName).isNull()
    }
}
