package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentRoleEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.entities.user.User
import org.locationtech.jts.geom.Coordinate
import java.time.Instant
import java.util.*

@UseCase
class FakeMissionData2(
    private val getFakeActionData: FakeActionData
) {
    fun getEnvControls(missionId: Int): List<MissionEnvActionEntity> {
        val envActionControl1 = MissionEnvActionEntity(
            envActionType = ActionTypeEnum.CONTROL,
            missionId = missionId,
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-7db642f99$missionId"),
            startDateTimeUtc = Instant.parse("2024-01-09T10:00:00Z"),
            endDateTimeUtc = null,  // Set to null if not provided in your API response
            controlPlans = listOf(
                EnvActionControlPlanEntity(
                    themeId = 1,
                    subThemeIds = listOf(1),
                    tagIds = listOf(1),
                )
            ),
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            actionNumberOfControls = 2,
            actionTargetType = ActionTargetTypeEnum.VEHICLE,
            vehicleType = VehicleTypeEnum.VESSEL,
            observations = "Observation",
            isComplianceWithWaterRegulationsControl = true,
            isSafetyEquipmentAndStandardsComplianceControl = true,
            isSeafarersControl = false,
            isAdministrativeControl = false,
            envInfractions = listOf(
                InfractionEntity(
                    id = "91200795-2823-46b3-8814-a5b3bca29$missionId",
                    natinf = listOf("4210", "28732"),
                    registrationNumber = "123ABC57",
                    companyName = null,
                    relevantCourt = null,
                    infractionType = InfractionTypeEnum.WITHOUT_REPORT,
                    formalNotice = FormalNoticeEnum.NO,
                    toProcess = false,
                    controlledPersonIdentity = "M. Bernard",
                    vesselType = VesselTypeEnum.COMMERCIAL,
                    vesselSize = VesselSizeEnum.FROM_24_TO_46m
                )
            )
        )

        val envActionSurveillance1 = MissionEnvActionEntity(
            envActionType = ActionTypeEnum.SURVEILLANCE,
            missionId = missionId,
            id = UUID.fromString("226d84bc-e6c5-4d29-8a5f-799642f99$missionId"),
            startDateTimeUtc = Instant.parse("2024-01-09T12:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-09T13:00:00Z"),
            controlPlans = listOf(
                EnvActionControlPlanEntity(
                    themeId = 2,
                    subThemeIds = listOf(2),
                    tagIds = listOf(2),
                )
            ),
            geom = createMockMultiPoint(listOf(Coordinate(-8.52318191, 48.30305604))),
            completion = ActionCompletionEnum.COMPLETED,
        )
        return listOf(envActionSurveillance1, envActionControl1)
    }

    fun fullMission(missionId: Int): MissionEntity2 {

        val envActions = getEnvControls(missionId)
        val fishActions = getFishActions(missionId)

        val commandant = AgentRoleEntity(1, "Commandant")
        val agentPont = AgentRoleEntity(2, "Agent pont")

        val gustave = AgentEntity(1, "Gustave", "Flaubert")
        val bob = AgentEntity(2, "Bob", "Mascouche")
        val crew1 = MissionCrewEntity(
            id = 1,
            agent = gustave,
            role = commandant,
            missionId = missionId
        )

        val crew2 = MissionCrewEntity(
            id = 2,
            agent = bob,
            role = agentPont,
            missionId = missionId
        )

        return MissionEntity2(
            id = missionId,
            envData = MissionEntity(
                missionTypes = listOf(MissionTypeEnum.SEA),
                missionSource = MissionSourceEnum.RAPPORTNAV,
                startDateTimeUtc = Instant.parse("2024-01-09T09:00:00Z"),
                endDateTimeUtc = Instant.parse("2024-01-09T15:00:00Z"),
                isDeleted = false,
                isGeometryComputedFromControls = false,
                hasMissionOrder = false,
                isUnderJdp = false,
                openBy = "fake",
                observationsByUnit = "Lors de la patrouille matinale, une concentration inhabituelle de déchets plastiques a été observée le long de la laisse de mer sur environ 300 mètres de rivage. Des fragments de filets de pêche, des bouteilles plastiques, et divers emballages ont été identifiés, laissant penser à un rejet en mer récent."
            ),
            actions = fishActions + envActions,
            generalInfos = MissionGeneralInfoEntity2(
                crew = listOf(crew1, crew2)
            )
        )
    }

    fun getFishActions(missionId: Int) = getFakeActionData.getFakeFishActions(missionId).filter {
        listOf(
            MissionActionType.SEA_CONTROL,
            MissionActionType.LAND_CONTROL
        ).contains(it.actionType)
    }.map { MissionFishActionEntity.fromFishAction(it) }

    fun emptyMission(missionId: Int): MissionEntity2 {
        return MissionEntity2(
            id = missionId,
            envData = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            missionSource = MissionSourceEnum.RAPPORTNAV,
            startDateTimeUtc = Instant.parse("2024-01-09T09:00:00Z"),
            endDateTimeUtc = Instant.parse("2024-01-09T15:00:00Z"),
            isDeleted = false,
            isGeometryComputedFromControls = false,
            hasMissionOrder = false,
            isUnderJdp = false,
            openBy = "fake"
            ),
            actions = listOf(),
        )
    }

    fun getFakeMissionsforUser(user: User?): List<MissionEntity2> {
        return when (user?.serviceId) {
            // bouteillon
            7 -> {
                listOf(fullMission(621), emptyMission(622))
            }
            // Ariane
            3 -> {
                listOf(fullMission(311), emptyMission(312))
            }
            // d'Alba
            6 -> {
                listOf(fullMission(761), emptyMission(762))
            }
            // else
            else -> {
                listOf()
            }
        }
    }
}
