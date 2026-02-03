package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.RequestLoggingConfig
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.web.filter.CommonsRequestLoggingFilter

class RequestLoggingConfigTest {

    @Test
    fun `should create CommonsRequestLoggingFilter bean`() {
        val config = RequestLoggingConfig()

        val filter = config.logFilter()

        assertNotNull(filter)
        assertTrue(filter is CommonsRequestLoggingFilter)
    }
}
