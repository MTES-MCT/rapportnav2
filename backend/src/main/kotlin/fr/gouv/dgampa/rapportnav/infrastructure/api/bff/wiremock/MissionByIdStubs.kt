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
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.n52.jackson.datatype.jts.JtsModule
import java.time.ZonedDateTime

object MissionByIdStubs {

    private val objectMapper: ObjectMapper = jacksonObjectMapper()

    fun configureStubs(wireMockServer: WireMockServer) {

        val resource1 = LegacyControlUnitResourceEntity(
            id = 1176,
            name = "Dacia Duster",
            controlUnitId = 10335
        )

        val resource2 = LegacyControlUnitResourceEntity(
            id = 1002,
            name = "Lanvéoc",
            controlUnitId = 10335
        )

        val controlUnit1 = LegacyControlUnitEntity(
            id = 10335,
            name = "FAKE-UNIT-1",
            isArchived = false,
            administration = "beta.gouv.fr",
            resources = mutableListOf(resource1, resource2)
        )


        val mission = MissionDataOutput(
            id = 1234,
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
            observationsByUnit = "Je suis une fake observation by units wiremock",
            geom = createMultiPolygon()
        )

        objectMapper.registerModules(mutableListOf(JavaTimeModule(), JtsModule()))

        val json = objectMapper.writeValueAsString(mission)

        wireMockServer.stubFor(
            WireMock.get(WireMock.urlMatching("/api/v1/missions/\\d+"))
                .willReturn(
                    WireMock.aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody(json)
                )
        )
    }
    private fun createMultiPolygon(): MultiPolygon {
        val geometryFactory = GeometryFactory()

        val coords1 = arrayOf(
            Coordinate(30.0, 20.0),
            Coordinate(45.0, 40.0),
            Coordinate(10.0, 40.0),
            Coordinate(30.0, 20.0)
        )
        val polygon1 = geometryFactory.createPolygon(coords1)

        val coords2 = arrayOf(
            Coordinate(15.0, 5.0),
            Coordinate(40.0, 10.0),
            Coordinate(10.0, 20.0),
            Coordinate(5.0, 10.0),
            Coordinate(15.0, 5.0)
        )
        val polygon2 = geometryFactory.createPolygon(coords2)

        return geometryFactory.createMultiPolygon(arrayOf(polygon1, polygon2))
    }
}
