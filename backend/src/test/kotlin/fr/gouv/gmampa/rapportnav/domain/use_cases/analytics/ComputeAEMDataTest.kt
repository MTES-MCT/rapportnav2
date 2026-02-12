package fr.gouv.gmampa.rapportnav.domain.use_cases.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMEnvTraffic
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMIllegalImmigration
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMOutOfMigrationRescue
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMTableExport
import fr.gouv.dgampa.rapportnav.domain.entities.aem.AEMVesselRescue
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.ComputeAEMData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.ExportMissionAEMSingle
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetComputeEnvMission
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionEntityMock
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [ComputeAEMData::class])
class ComputeAEMDataTest {

    @Autowired
    private lateinit var computeAEMData: ComputeAEMData

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var exportMissionAEMSingle: ExportMissionAEMSingle

    @MockitoBean
    private lateinit var getComputeEnvMission: GetComputeEnvMission

    private var mockTableExport = AEMTableExport(
        outOfMigrationRescue = AEMOutOfMigrationRescue(
            nbrOfHourAtSea = 1.0,
            nbrOfRescuedOperation = 1.0,
            nbrPersonsRescued = 1.0,
        ),
        migrationRescue = AEMMigrationRescue(
            nbrOfHourAtSea = 1.0,
        ),
        vesselRescue = AEMVesselRescue(
            nbrOfNoticedVessel = 18.0,
            nbrOfTowedVessel = 3.0
        ),
        envTraffic = AEMEnvTraffic(
            nbrOfHourAtSea = 1.0,
        ),
        illegalImmigration = AEMIllegalImmigration(
            nbrOfHourAtSea = 1.0,
        ),
        notPollutionControlSurveillance = null,
        pollutionControlSurveillance = null,
        illegalFish = null,
        culturalMaritime = null,
        seaSafety = null,
        sovereignProtect = null,
    )

    @Test
    fun `Should return the correct result`() {
        val missionId = 123
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(
                id = missionId,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z"),
            )
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(
                id = missionId,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z"),
            )
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(mockTableExport)

        val actual = computeAEMData.execute(missionId = missionId)
        assertThat(actual?.id).isEqualTo(missionId)
        assertThat(actual?.startDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T12:00:00Z"))
        assertThat(actual?.endDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T13:00:00Z"))
        assertThat(actual?.facade).isEqualTo(null)
        assertThat(actual?.missionTypes).isEqualTo(listOf<MissionTypeEnum>())
        assertThat(actual?.controlUnits).isEqualTo(listOf<LegacyControlUnitEntity>())
        assertThat(actual?.isDeleted).isEqualTo(false)
        assertThat(actual?.missionSource).isEqualTo(MissionSourceEnum.MONITORENV)
        assertThat(actual?.completenessForStats).isNotNull()
        assertThat(actual?.isMissionFinished).isTrue() // endDateTimeUtc is in the past
        assertThat(actual?.data?.size).isGreaterThan(30)
        assertThat(actual?.data?.any { it?.value == 1.0 }).isTrue()
    }

    @Test
    fun `Should return null when no AEM data found`() {
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(null)
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(null)

        val result = computeAEMData.execute(999)
        assertThat(result).isNull()
    }

    @Test
    fun `Should handle null metrics and fill zeros`() {
        val missionId = 1
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(id = missionId)
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(id = missionId)
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(
            AEMTableExport(
                outOfMigrationRescue = null,
                migrationRescue = null,
                vesselRescue = null,
                envTraffic = null,
                illegalImmigration = null,
                notPollutionControlSurveillance = null,
                pollutionControlSurveillance = null,
                illegalFish = null,
                culturalMaritime = null,
                seaSafety = null,
                sovereignProtect = null,
            )
        )

        val result = computeAEMData.execute(missionId)
        assertThat(result).isNotNull
        assertThat(result!!.data.all { it?.value == 0.0 }).isTrue()
    }

    @Test
    fun `Should include general info fields when present`() {
        val missionId = 123
        val generalInfo = MissionGeneralInfoEntityMock.create(
            missionId = missionId,
            missionIdUUID = UUID.fromString("135689d8-3163-426c-aa26-e20eb9eb5f2e"),
            service = ServiceEntityMock.create(id = 42)
        )

        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(
                id = missionId,
                generalInfos = MissionGeneralInfoEntity2(data = generalInfo)
            )
        )
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(
            AEMTableExport(
                outOfMigrationRescue = null,
                migrationRescue = null,
                vesselRescue = null,
                envTraffic = null,
                illegalImmigration = null,
                notPollutionControlSurveillance = null,
                pollutionControlSurveillance = null,
                illegalFish = null,
                culturalMaritime = null,
                seaSafety = null,
                sovereignProtect = null,
            )
        )

        val result = computeAEMData.execute(missionId)

        assertThat(result?.idUUID).isEqualTo(UUID.fromString("135689d8-3163-426c-aa26-e20eb9eb5f2e"))
        assertThat(result?.serviceId).isEqualTo(42)
    }

    @Test
    fun `Should return isMissionFinished false when endDateTimeUtc is in the future`() {
        val missionId = 123
        val futureDate = Instant.now().plusSeconds(86400) // tomorrow

        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(
                id = missionId,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = futureDate,
            )
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(
                id = missionId,
                endDateTimeUtc = futureDate,
            )
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(mockTableExport)

        val result = computeAEMData.execute(missionId)

        assertThat(result?.isMissionFinished).isFalse()
    }

    @Test
    fun `Should return isMissionFinished true when endDateTimeUtc is in the past`() {
        val missionId = 123
        val pastDate = Instant.parse("2022-01-02T13:00:00Z")

        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(
                id = missionId,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = pastDate,
            )
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(
                id = missionId,
                endDateTimeUtc = pastDate,
            )
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(mockTableExport)

        val result = computeAEMData.execute(missionId)

        assertThat(result?.isMissionFinished).isTrue()
    }

    @Test
    fun `Should return completenessForStats from mission`() {
        val missionId = 123

        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(id = missionId)
        )
        `when`(getComputeEnvMission.execute(missionId)).thenReturn(
            MissionEntityMock.create(id = missionId)
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle.getAemData(any())).thenReturn(mockTableExport)

        val result = computeAEMData.execute(missionId)

        assertThat(result?.completenessForStats).isNotNull()
        assertThat(result?.completenessForStats?.status).isIn(
            CompletenessForStatsStatusEnum.COMPLETE,
            CompletenessForStatsStatusEnum.INCOMPLETE
        )
    }
}
