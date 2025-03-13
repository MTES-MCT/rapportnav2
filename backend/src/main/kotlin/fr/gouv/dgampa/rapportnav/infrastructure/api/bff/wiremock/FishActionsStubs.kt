package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData

object FishActionsStubs {

    fun configureStubs(wireMockServer: WireMockServer) {
        val json = LoadJsonData.load("fish/actions.json")

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching("/api/v1/mission_actions"))
                .withQueryParam("missionId", WireMock.matching(".*"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
}
