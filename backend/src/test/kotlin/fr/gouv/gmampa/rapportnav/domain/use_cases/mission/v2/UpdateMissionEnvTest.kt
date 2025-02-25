package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateMissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [UpdateMissionEnv::class])
class UpdateMissionEnvTest {

    @Autowired
    private lateinit var updateMissionEnv: UpdateMissionEnv

    @MockitoBean
    private lateinit var envMissionRepo: IEnvMissionRepository

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var apiEnvRepo2: APIEnvMissionRepositoryV2


    @Test
    fun `execute should return null when MissionEntity are equal`() {
        val missionId = 123
        // Given
        val mockMissionEntity = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            hasMissionOrder = false,
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            id = missionId,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        val missionEnvEntity = MissionEnvEntity(
            id = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)
        Mockito.`when`(apiEnvRepo2.update(MissionEnv.fromMissionEnvEntity(missionEnvEntity))).thenReturn(missionEnvEntity)


        // When
        val result = updateMissionEnv.execute(missionEnvEntity)

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `execute should return MissionEnvEntity when MissionEntity are not equal`() {
        val missionId = 123
        // Given
        val mockMissionEntity = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            hasMissionOrder = false,
            envActions = listOf(),
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            id = missionId,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        val missionEnvEntity = MissionEnvEntity(
            id = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            observationsByUnit = "observation updated" +
                "",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)
        Mockito.`when`(apiEnvRepo2.update(MissionEnv.fromMissionEnvEntity(missionEnvEntity))).thenReturn(missionEnvEntity)


        // When
        val result = updateMissionEnv.execute(missionEnvEntity)

        // Then
        Assertions.assertNotNull(result)
    }

    @Test
    fun `execute should return null when MissionEntity is equal because we updated a not patchable property`() {
        val missionId = 123
        // Given
        val mockMissionEntity = MissionEntity(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            hasMissionOrder = false,
            envActions = listOf(),
            isDeleted = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            id = missionId,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        val missionEnvEntity = MissionEnvEntity(
            id = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            missionSource = MissionSourceEnum.MONITORFISH,
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)
        Mockito.`when`(apiEnvRepo2.update(MissionEnv.fromMissionEnvEntity(missionEnvEntity))).thenReturn(missionEnvEntity)


        // When
        val result = updateMissionEnv.execute(missionEnvEntity)

        // Then
        Assertions.assertNull(result)
    }
}
