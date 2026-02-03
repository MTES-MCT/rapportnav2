package fr.gouv.gmampa.rapportnav.config

import fr.gouv.dgampa.rapportnav.config.HttpClientFactory
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.net.http.HttpClient

class HttpClientFactoryTest {

    @Test
    fun `should create an HttpClient instance`() {
        val factory = HttpClientFactory()

        val client = factory.create()

        assertNotNull(client)
    }
}
