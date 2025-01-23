package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import java.time.ZonedDateTime

object WireMockStubs {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun configureStubs(wireMockServer: WireMockServer) {

        val mission = MissionDataOutput(
            id = 1,
            missionSource = MissionSourceEnum.MONITORENV,
            hasMissionOrder = false,
            isGeometryComputedFromControls = false,
            isUnderJdp = false,
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(),
            startDateTimeUtc = ZonedDateTime.now(),
            endDateTimeUtc = null,
            openBy = "local-mock"
        )

        val missions = listOf(mission)

        objectMapper.registerModule(JavaTimeModule())

        val json = objectMapper.writeValueAsString(missions)

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
