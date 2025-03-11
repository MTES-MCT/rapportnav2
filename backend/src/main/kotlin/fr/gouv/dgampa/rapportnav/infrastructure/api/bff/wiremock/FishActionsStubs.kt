package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import java.nio.file.Files
import java.nio.file.Paths

object FishActionsStubs {

    fun configureStubs(wireMockServer: WireMockServer) {
        val path = Paths.get("src/main/resources/wiremock/fish/actions.json")
        val json = String(Files.readAllBytes(path))

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
