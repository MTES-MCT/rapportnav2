package fr.gouv.dgampa.rapportnav.config

import com.github.tomakehurst.wiremock.WireMockServer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.*
import org.slf4j.LoggerFactory


class WireMockConfig {

    private val logger = LoggerFactory.getLogger(WireMockConfig::class.java)

    fun startWireMock(): WireMockServer {
        val wireMockServer = WireMockServer(8089)
        wireMockServer.start()

        logger.info("WireMock server started on http://localhost:8089")

        MissionsStubs.configureStubs(wireMockServer)
        AdministrationStubs.configureStubs(wireMockServer)
        NatinfStubs.configureStubs(wireMockServer)
        ControlUnitResourcesStubs.configureStubs(wireMockServer)
        FishActionsStubs.configureStubs(wireMockServer)
        MissionByIdStubs.configureStubs(wireMockServer)
        FishVesselsStubs.configureStubs(wireMockServer)

        return wireMockServer
    }
}
