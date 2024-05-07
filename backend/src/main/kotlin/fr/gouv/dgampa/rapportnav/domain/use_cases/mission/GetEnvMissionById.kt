package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.EnvMission
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.AttachControlsToActionControl
import io.sentry.Sentry
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.MultiPoint
import org.n52.jackson.datatype.jts.JtsModule
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.ZonedDateTime
import java.util.*

fun createMockMultiPoint(coordinates: List<Coordinate>): MultiPoint {
    val factory = GeometryFactory()
    val points = coordinates.map { factory.createPoint(it) }
    return factory.createMultiPoint(points.toTypedArray())
}

@UseCase
class GetEnvMissionById(
    private val mapper: ObjectMapper,
    private val getEnvMissions: GetEnvMissions,
    private val attachControlsToActionControl: AttachControlsToActionControl,
) {
    private val logger = LoggerFactory.getLogger(GetEnvMissionById::class.java)

    private fun getFakeSurveillance(): List<EnvActionSurveillanceEntity> {
        val envMissionActionSurveillance1 = EnvActionSurveillanceEntity(
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d99"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-17T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-17T06:50:09Z"),
            completion = ActionCompletionEnum.COMPLETED
        )

        return listOf(envMissionActionSurveillance1)
    }

    private fun getFakeControls(): List<EnvActionControlEntity> {
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


        val envActionControl1 = EnvActionControlEntity(
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99d16"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2023-12-15T10:00:00Z"),
            actionEndDateTimeUtc = null,  // Set to null if not provided in your API response
            themes = listOf(
                ThemeEntity(
                    theme = "Divers",
                    subThemes = listOf("Autres contrôles (spécifier dans les observations)"),
                    protectedSpecies = emptyList()
                )
            ),
            completion = ActionCompletionEnum.COMPLETED,
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 1,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "Observation test",
            isSeafarersControl = false,  // Adjust based on your API response
            isAdministrativeControl = false,  // Adjust based on your API response
            infractions = listOf(
                InfractionEntity(
                    id = "91200795-2823-46b3-8814-a5b3bca29a47",
                    natinf = listOf("0", "118"),
                    registrationNumber = "kjhfieohfgs",
                    companyName = null,
                    relevantCourt = null,
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = false,
                    controlledPersonIdentity = "Mr Loutre",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.FROM_24_TO_46m
                )
            )
        )

        var envMissionActionControl1 = EnvActionControlEntity(
            id = UUID.fromString("17997de8-b0df-4095-b209-e8758df71b67"),
            actionStartDateTimeUtc = ZonedDateTime.parse("2022-02-16T04:50:09Z"),
            actionEndDateTimeUtc = ZonedDateTime.parse("2022-02-16T06:50:09Z"),
            themes = listOf(controlTheme1),
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 5,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "blablabla",
            isSeafarersControl = true,
            isAdministrativeControl = true,
            completion = ActionCompletionEnum.COMPLETED,
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
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionTargetType = null,
            vehicleType = null,
            observations = null,
            isSeafarersControl = true,
            isAdministrativeControl = true,
            completion = ActionCompletionEnum.COMPLETED,
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
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 8,
            actionTargetType = ActionTargetTypeEnum.INDIVIDUAL,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = null,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isComplianceWithWaterRegulationsControl = true,
            infractions = listOf(),
        )

        return listOf(envActionControl1, envMissionActionControl1, envMissionActionControl2, envMissionActionControl3)
    }

    fun getMissionWithControls(envMission: MissionEntity): ExtendedEnvMissionEntity {
        var mission = ExtendedEnvMissionEntity.fromEnvMission(envMission)

        // need to attach nav controls to calculate correctly the completeness
        mission.actions?.map {
            if (it?.controlAction != null) {
                val actionWithControls = attachControlsToActionControl.toEnvAction(
                    actionId = it.controlAction.action?.id.toString(),
                    action = it
                )
                actionWithControls.controlAction?.computeCompleteness()
                ExtendedEnvActionEntity(
                    controlAction = actionWithControls.controlAction
                )
            } else {
                // no need for Surveillances
                ExtendedEnvActionEntity(
                    surveillanceAction = it?.surveillanceAction
                )
            }
        }
        return mission
    }

    fun execute(missionId: Int): ExtendedEnvMissionEntity? {

        try {
            val client: HttpClient = HttpClient.newBuilder().build()
            val request = HttpRequest
                .newBuilder()
                .uri(
                    URI.create(
                        "https://monitorenv.din.developpement-durable.gouv.fr/api/v1/missions/$missionId"
                    )
                )
                .build()

            val response = client.send(request, HttpResponse.BodyHandlers.ofString())

            mapper.registerModule(JtsModule())

            val envMission = mapper.readValue(response.body(), object : TypeReference<EnvMission>() {})
            var mission = getMissionWithControls(envMission)
            return mission
        } catch (e: Exception) {
            logger.error("GetEnvMissionById failed loading EnvMission", e)
            Sentry.captureMessage("GetEnvMissionById failed loading EnvMission")
            Sentry.captureException(e)
            return null
//            var envMission = getEnvMissions.mockedMissions.find { missionId == it.id }!!
//            envMission.envActions = this.getFakeControls() + this.getFakeSurveillance()
//            var mission = getMissionWithControls(envMission)
//            return mission
        }


    }
}
