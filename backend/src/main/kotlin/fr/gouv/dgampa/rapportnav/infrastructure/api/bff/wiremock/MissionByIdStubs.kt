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
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTargetTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.MissionDataOutput
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPolygon
import org.n52.jackson.datatype.jts.JtsModule
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import kotlin.String
import kotlin.collections.List

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
        val controlUnit2 = LegacyControlUnitEntity(
            id = 10141,
            name = "FAKE-PAM-UNIT-1",
            isArchived = false,
            administration = "DIRMNAMO",
            resources = mutableListOf(resource1, resource2)
        )

        val envControlVehicle = EnvActionControlEntity(
            id = UUID.fromString("fbbd34c5-94d1-4020-8351-c100bbd62b0a"),
            actionStartDateTimeUtc = Instant.now().minus(1L, ChronoUnit.DAYS),
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            geom = createMultiPolygon(),
        )
        val envControlIndividual = EnvActionControlEntity(
            id = UUID.fromString("0d548f2d-e5cb-4ac4-bf64-68d344f6bbb7"),
            actionStartDateTimeUtc = Instant.now().minus(1L, ChronoUnit.DAYS).plus(1L, ChronoUnit.HOURS),
            actionEndDateTimeUtc = Instant.now().minus(1L, ChronoUnit.DAYS).plus(2L, ChronoUnit.HOURS),
            actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
            geom = createMultiPolygon(),
            actionNumberOfControls = 1,
            infractions = listOf(InfractionEntity(
                id =  "94f7d9fb-b990-4183-8f91-f32b2159435a",
                natinf = listOf(),
                observations = "RAS",
                controlledPersonIdentity = "Robin Dupont",
                infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                formalNotice = FormalNoticeEnum.NO,
                toProcess = false,
            ))
        )
        val envControlCompany = EnvActionControlEntity(
            id = UUID.fromString("60fbf0f7-5887-467f-8b6f-6dad27afc82c"),
            actionStartDateTimeUtc = Instant.now().minus(1L, ChronoUnit.DAYS).plus(2L, ChronoUnit.HOURS),
            actionEndDateTimeUtc = Instant.now().minus(3L, ChronoUnit.DAYS).plus(2L, ChronoUnit.HOURS),
            actionTargetType = ActionTargetTypeEnum.COMPANY,
            geom = createMultiPolygon(),
            actionNumberOfControls = 2,
            infractions = listOf(InfractionEntity(
                 id =  "53bd7fd7-44d9-460b-8922-f9102571233a",
                 natinf = listOf("10000"),
                 observations = "calibrage filet incorrect",
                 registrationNumber = "FR5454DR",
                 companyName = "SARL Douce Mer",
                 infractionType = InfractionTypeEnum.WITH_REPORT,
                 formalNotice = FormalNoticeEnum.YES,
                 toProcess = false,
            ),
                InfractionEntity(
                    id = "d3a574c0-2133-4859-96e4-62c671943c76",
                    natinf = listOf(),
                    observations = "RAS",
                    registrationNumber = "FR333ZA",
                    companyName = "SARL Plaisance44",
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = false,
                ))
        )


        val mission = MissionDataOutput(
            id = 1234,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            hasMissionOrder = false,
            isGeometryComputedFromControls = false,
            isUnderJdp = false,
            missionTypes = listOf(MissionTypeEnum.LAND),
            controlUnits = listOf(controlUnit1, controlUnit2),
            startDateTimeUtc = ZonedDateTime.now().minus(2L, ChronoUnit.DAYS),
            endDateTimeUtc = null,
            openBy = "local-mock",
            observationsCacem = "Je suis une fake observation wiremock",
            observationsByUnit = "Je suis une fake observation by units wiremock",
            geom = createMultiPolygon(),
            envActions = listOf(envControlVehicle, envControlIndividual, envControlCompany)
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
