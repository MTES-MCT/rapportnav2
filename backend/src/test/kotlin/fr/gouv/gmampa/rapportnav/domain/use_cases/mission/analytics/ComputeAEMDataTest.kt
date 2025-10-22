package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMEnvTraffic2
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMIllegalImmigration2
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMMigrationRescue2
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMOutOfMigrationRescue2
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMTableExport2
import fr.gouv.dgampa.rapportnav.domain.entities.aem.v2.AEMVesselRescue2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.analytics.ComputeAEMData
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2.ExportMissionAEMSingle2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.`when`
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
    private lateinit var exportMissionAEMSingle2: ExportMissionAEMSingle2

    private var mockTableExport = AEMTableExport2(
        outOfMigrationRescue = AEMOutOfMigrationRescue2(
            nbrOfHourAtSea = 1.0,
            nbrOfRescuedOperation = 1.0,
            nbrPersonsRescued = 1.0,
        ),
        migrationRescue = AEMMigrationRescue2(
            nbrOfHourAtSea = 1.0,
        ),
        vesselRescue = AEMVesselRescue2(
            nbrOfNoticedVessel = 18.0,
            nbrOfTowedVessel = 3.0
        ),
        envTraffic = AEMEnvTraffic2(
            nbrOfHourAtSea = 1.0,
        ),
        illegalImmigration = AEMIllegalImmigration2(
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
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(
                id = 123,
                startDateTimeUtc = Instant.parse("2022-01-02T12:00:00Z"),
                endDateTimeUtc = Instant.parse("2022-01-02T13:00:00Z"),
            )
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle2.getAemTableExport(anyList())).thenReturn(listOf(mockTableExport))

        val actual = computeAEMData.execute(missionId = 123)
        assertThat(actual?.id).isEqualTo(123)
        assertThat(actual?.startDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T12:00:00Z"))
        assertThat(actual?.endDateTimeUtc).isEqualTo(Instant.parse("2022-01-02T13:00:00Z"))
        assertThat(actual?.facade).isEqualTo(null)
        assertThat(actual?.missionTypes).isEqualTo(listOf<MissionTypeEnum>())
        assertThat(actual?.controlUnits).isEqualTo(listOf<LegacyControlUnitEntity>())
        assertThat(actual?.isDeleted).isEqualTo(false)
        assertThat(actual?.missionSource).isEqualTo(MissionSourceEnum.MONITORENV)
        assertThat(actual?.data?.size).isGreaterThan(30)
        assertThat(actual?.data?.any { it.value == 1.0 }).isTrue()
    }

    @Test
    fun `Should return null when no AEM data found`() {
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(null)
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle2.getAemTableExport(anyList())).thenReturn(emptyList())

        val result = computeAEMData.execute(999)
        assertThat(result).isNull()
    }

    @Test
    fun `Should handle null metrics and fill zeros`() {
        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(id = 1)
        )
        `when`(getMissionGeneralInfoByMissionId.execute(anyInt())).thenReturn(null)
        `when`(exportMissionAEMSingle2.getAemTableExport(anyList())).thenReturn(listOf(
            AEMTableExport2(
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
        ))

        val result = computeAEMData.execute(1)
        assertThat(result).isNotNull
        assertThat(result!!.data.all { it.value == 0.0 }).isTrue()
    }

    @Test
    fun `Should include general info fields when present`() {
        val missionId = 123
        val generalInfo = MissionGeneralInfoEntity(
            missionId = missionId,
            missionIdUUID = UUID.fromString("135689d8-3163-426c-aa26-e20eb9eb5f2e"),
            serviceId = 42
        )

        `when`(getEnvMissionById2.execute(anyInt())).thenReturn(
            EnvMissionMock.create(id = missionId)
        )
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(generalInfo)
        `when`(exportMissionAEMSingle2.getAemTableExport(anyList())).thenReturn(listOf(
            AEMTableExport2(
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
        ))

        val result = computeAEMData.execute(missionId)

        assertThat(result?.idUUID).isEqualTo(UUID.fromString("135689d8-3163-426c-aa26-e20eb9eb5f2e"))
        assertThat(result?.serviceId).isEqualTo(42)
    }
}
