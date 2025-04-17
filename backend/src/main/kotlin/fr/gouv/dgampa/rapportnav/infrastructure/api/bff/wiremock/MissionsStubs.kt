package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import java.time.ZonedDateTime

object MissionsStubs {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun configureStubs(wireMockServer: WireMockServer) {

        val resource1 = LegacyControlUnitResourceEntity(
            id = 1,
            name = "Dacia Duster",
            controlUnitId = 10317
        )

        val resource2 = LegacyControlUnitResourceEntity(
            id = 2,
            name = "Toyota Hilux",
            controlUnitId = 10317
        )

        val controlUnit1 = LegacyControlUnitEntity(
            id = 10317,
            name = "FAKE-UNIT-1",
            isArchived = false,
            administration = "beta.gouv.fr",
            resources = mutableListOf(resource1, resource2)
        )


        val mission1 = MissionDataOutput(
            id = "1234",
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            hasMissionOrder = false,
            isGeometryComputedFromControls = false,
            isUnderJdp = false,
            missionTypes = listOf(MissionTypeEnum.LAND),
            controlUnits = listOf(controlUnit1),
            startDateTimeUtc = ZonedDateTime.now(),
            endDateTimeUtc = null,
            openBy = "local-mock",
            observationsCacem = "Je suis une fake observation wiremock",
            observationsByUnit = "Je suis une fake observation by units wiremock"
        )

        val mission2 = MissionDataOutput(
            id = "5678",
            missionSource = MissionSourceEnum.MONITORENV,
            hasMissionOrder = false,
            isGeometryComputedFromControls = false,
            isUnderJdp = false,
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(),
            startDateTimeUtc = ZonedDateTime.now(),
            endDateTimeUtc = null,
            openBy = "local-mock",
            observationsCnsp = "Je suis une fake observation wiremock"
        )

        val missions = listOf(mission1, mission2)

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
