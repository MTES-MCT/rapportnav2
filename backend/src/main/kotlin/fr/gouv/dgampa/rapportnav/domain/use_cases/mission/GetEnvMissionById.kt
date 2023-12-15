package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.user.GetControlUnitsForUser
import io.sentry.Sentry
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*


@UseCase
class GetEnvMissionById(
    private val mapper: ObjectMapper
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissionById::class.java)

    fun execute(missionId: Int): ExtendedEnvMissionEntity? {

        try {
            val client: HttpClient = HttpClient.newBuilder().build()
            val request = HttpRequest.newBuilder()
                .uri(
                    URI.create(
                        "https://monitorenv.din.developpement-durable.gouv.fr/api/v1/missions/$missionId"
                    )
                )
                .build();

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            mapper.registerModule(JtsModule())

            val envMission = mapper.readValue(response.body(), object : TypeReference<EnvMission>() {})
            return ExtendedEnvMissionEntity.fromEnvMission(envMission)
        } catch (e: Exception) {
            logger.error("GetEnvMissionById failed loading Mission", e)
            Sentry.captureMessage("GetEnvMissionById failed loading Mission")
            Sentry.captureException(e)

            val controlTheme1 = ThemeEntity(
                theme = "Rejets illicites",
                subThemes = listOf("avitaillement", "soutage"),
                protectedSpecies = listOf("fish1", "fish2")
            )
            val controlTheme2 = ThemeEntity(
                theme = "Accès zone protégée",
                subThemes = listOf("avitaillement", "soutage"),
                protectedSpecies = listOf("fish1", "fish2")
            )
            var envMissionActionControl1 = EnvActionControlEntity(
                id = UUID.fromString("17997de8-b0df-4095-b209-e8758df71b67"),
                actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-16T04:50:09Z"),
                actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-16T06:50:09Z"),
                themes = listOf(controlTheme1),
                geom = GeometryFactory().createPoint(Coordinate(52.4, 14.2)),
                actionNumberOfControls = 5,
                actionTargetType = ActionTargetTypeEnum.VEHICLE,
                vehicleType = VehicleTypeEnum.VESSEL,
                observations = "blablabla",
                isSeafarersControl = true,
                isAdministrativeControl = true,
                infractions = listOf(
                    InfractionEntity(
                        id = "12341",
                        natinf = listOf("2593", "27773"),
                        registrationNumber = "09AB23",
                        companyName = "dummy company",
                        relevantCourt = "tribunal",
                        infractionType = InfractionTypeEnum.WITH_REPORT,
                        formalNotice = FormalNoticeEnum.YES,
                        toProcess = true,
                        controlledPersonIdentity = "Jean Robert",
                        vesselType = VesselTypeEnum.COMMERCIAL,
                        vesselSize = VesselSizeEnum.LESS_THAN_12m,
                    )
                ),
            )

            val envMissionActionControl2 = EnvActionControlEntity(
                id = UUID.fromString("aa997de8-b0df-3095-b209-e8758df71bbb"),
                actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-19T04:50:09Z"),
                actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-19T06:50:09Z"),
                themes = listOf(controlTheme1),
                actionNumberOfControls = 10,
                geom = GeometryFactory().createPoint(Coordinate(52.4, 14.2)),
                actionTargetType = null,
                vehicleType = null,
                observations = null,
                isSeafarersControl = true,
                isAdministrativeControl = true,
                infractions = listOf(
                    InfractionEntity(
                        id = "12342",
                        natinf = listOf("2593", "27773"),
                        registrationNumber = "27773",
                        companyName = "dummy company",
                        relevantCourt = "tribunal",
                        infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                        formalNotice = FormalNoticeEnum.NO,
                        toProcess = true,
                        controlledPersonIdentity = "Jean Robert",
                        vesselType = VesselTypeEnum.COMMERCIAL,
                        vesselSize = VesselSizeEnum.LESS_THAN_12m,
                    )
                ),
            )

            val envMissionActionControl3 = EnvActionControlEntity(
                id = UUID.fromString("aa997de8-b0df-4095-b209-e8758df71bbb"),
                actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-21T04:50:09Z"),
                actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-21T06:50:09Z"),
                themes = listOf(controlTheme2),
                geom = GeometryFactory().createPoint(Coordinate(52.4, 14.2)),
                actionNumberOfControls = 8,
                actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
                vehicleType = VehicleTypeEnum.VESSEL,
                observations = null,
                isSafetyEquipmentAndStandardsComplianceControl = true,
                isComplianceWithWaterRegulationsControl = true,
                infractions = listOf(),
            )
//        val envMissionActionSurveillance = EnvActionSurveillanceEntity(
//            id = UUID.randomUUID(),
//            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-17T04:50:09Z"),
//            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-17T06:50:09Z")
//        )
//        val envMissionActionNote = EnvActionNoteEntity(
//            id = UUID.randomUUID(),
//            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-18T04:50:09Z"),
//            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-18T06:50:09Z")
//        )
            val envMission = MissionEntity(
                id = 10,
                missionTypes = listOf(MissionTypeEnum.SEA),
                facade = "Outre-Mer",
                observationsCacem = null,
                startDateTimeUtc = ZonedDateTime.parse("2022-02-15T04:50:09Z"),
//            endDateTimeUtc = ZonedDateTime.parse("2022-02-23T20:29:03Z"),
                isClosed = false,
                isDeleted = false,
                missionSource = MissionSourceEnum.RAPPORTNAV,
                hasMissionOrder = false,
                isUnderJdp = false,
                isGeometryComputedFromControls = false,
                envActions = listOf(envMissionActionControl1, envMissionActionControl2, envMissionActionControl3)
            )

            // 2. MonitorFish - Get and add up MissionActions
            // TODO replace with API call


            return ExtendedEnvMissionEntity.fromEnvMission(envMission)
        }


    }
}
