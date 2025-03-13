package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData

object MissionsStubs {

    fun configureStubs(wireMockServer: WireMockServer) {

        val json = LoadJsonData.load("env/missions.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlPathEqualTo("/api/v1/missions"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
