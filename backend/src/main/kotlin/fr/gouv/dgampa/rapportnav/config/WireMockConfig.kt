package fr.gouv.dgampa.rapportnav.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.WireMockStubs

class WireMockConfig {

    fun startWireMock(): WireMockServer {
        val wireMockServer = WireMockServer(WireMockConfiguration.wireMockConfig().port(8089))
        wireMockServer.start()

        println("WireMock server started on http://localhost:8089")

        WireMockStubs.configureStubs(wireMockServer)

        return wireMockServer
    }
}
