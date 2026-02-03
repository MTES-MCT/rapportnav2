package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.CorsConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.CorsFilter

class CorsConfigTest {

    @Test
    fun `should create CorsFilter bean`() {
        val config = CorsConfig()

        val filter = config.corsFilter()

        assertNotNull(filter)
        assertTrue(filter is CorsFilter)
    }
}
