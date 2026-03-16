package fr.gouv.dgampa.rapportnav.config

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.*
import org.slf4j.LoggerFactory


class WireMockConfig {

    private val logger = LoggerFactory.getLogger(WireMockConfig::class.java)

    fun startWireMock(): WireMockServer {
        val wireMockServer = WireMockServer(
            WireMockConfiguration.options()
                .port(8089)
                .globalTemplating(true)
        )
        wireMockServer.start()

        logger.info("WireMock server started on http://localhost:8089")

        MissionsStubs.configureStubs(wireMockServer)
        AdministrationStubs.configureStubs(wireMockServer)
        NatinfStubs.configureStubs(wireMockServer)
        ControlUnitsStubs.configureStubs(wireMockServer)
        ControlUnitResourcesStubs.configureStubs(wireMockServer)
        FishActionsStubs.configureStubs(wireMockServer)
        MissionByIdStubs.configureStubs(wireMockServer)
        FishVesselsStubs.configureStubs(wireMockServer)
        FishPortsStubs.configureStubs(wireMockServer)
        ActionStubs.configureStubs(wireMockServer)

        return wireMockServer
    }
}
