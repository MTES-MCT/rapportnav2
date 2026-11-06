package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData

object NatinfStubs {

    fun configureStubs(wireMockServer: WireMockServer) {

        val json = LoadJsonData.load("fish/infractions.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/v1/infractions"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
