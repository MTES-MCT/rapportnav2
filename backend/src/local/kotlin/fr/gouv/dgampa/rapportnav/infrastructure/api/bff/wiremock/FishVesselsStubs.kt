package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData

object FishVesselsStubs {

    fun configureStubs(wireMockServer: WireMockServer) {
        val json = LoadJsonData.load("fish/vessels.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching("/api/v1/vessels"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
