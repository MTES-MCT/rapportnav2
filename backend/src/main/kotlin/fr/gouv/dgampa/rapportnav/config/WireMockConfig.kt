package fr.gouv.dgampa.rapportnav.config

import com.github.tomakehurst.wiremock.WireMockServer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.*


class WireMockConfig {

    fun startWireMock(): WireMockServer {
        val wireMockServer = WireMockServer(8089)
        wireMockServer.start()

        println("WireMock server started on http://localhost:8089")

        MissionsStubs.configureStubs(wireMockServer)
        AdministrationStubs.configureStubs(wireMockServer)
        NatinfStubs.configureStubs(wireMockServer)
        ControlUnitResourcesStubs.configureStubs(wireMockServer)
        MissionByIdStubs.configureStubs(wireMockServer)
        FishActionsStubs.configureStubs(wireMockServer)
        ControlPlansStubs.configureStubs(wireMockServer)

        return wireMockServer
    }
}
