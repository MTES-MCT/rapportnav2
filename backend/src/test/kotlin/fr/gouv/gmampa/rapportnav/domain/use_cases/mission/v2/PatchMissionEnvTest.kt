package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.PatchMissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.APIEnvMissionRepositoryV2
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Mockito
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [PatchMissionEnv::class])
class PatchMissionEnvTest {

    @Autowired
    private lateinit var patchMissionEnv: PatchMissionEnv

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @MockitoBean
    private lateinit var getEnvMissionById2: GetEnvMissionById2

    @MockitoBean
    private lateinit var apiEnvRepo2: APIEnvMissionRepositoryV2


    @Test
    fun `execute should return null when MissionEntity are equal`() {
        val missionId = 123
        val missionEntity = getMockMissionEntity(missionId = missionId, controlUnitId = 12)
        val missionEnvEntity = getMissionEnvEntity(missionId = missionId, controlUnitId = 12)
        val input = MissionEnvInput(
            isUnderJdp = false,
            missionId = missionId,
            resources = listOf(),
            missionTypes = listOf(MissionTypeEnum.SEA),
            endDateTimeUtc = missionEntity.endDateTimeUtc,
            startDateTimeUtc = missionEntity.startDateTimeUtc,
            observationsByUnit = missionEntity.observationsByUnit,
        )

        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(missionEntity)
        Mockito.`when`(apiEnvRepo2.patchMission(anyInt(), anyOrNull())).thenReturn(missionEnvEntity)

        // When
        val result = patchMissionEnv.execute(input)

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `execute should return MissionEnvEntity when MissionEntity are not equal`() {
        val missionId = 123

        // Given
        val missionEntity = getMockMissionEntity(missionId = missionId, controlUnitId = 12)
        val missionEnvEntity = getMissionEnvEntity(missionId = missionId, controlUnitId = 12)
        val input = MissionEnvInput(
            missionId = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            resources = listOf(LegacyControlUnitResourceEntity(id = 1233, controlUnitId = 14))
        )
        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(missionEntity)
        Mockito.`when`(apiEnvRepo2.patchMission(anyInt(), anyOrNull())).thenReturn(missionEnvEntity)

        // When
        val result = patchMissionEnv.execute(input)
        // Then
        Assertions.assertNotNull(result)
    }

    @Test
    fun `execute should not throw Exception when resources are empty`() {
        val missionId = 123
        val missionEnvEntity = getMissionEnvEntity(missionId = missionId, controlUnitId = 12)
        val missionEntity = getMockMissionEntity(missionId = missionId, controlUnitId = 12, withoutResource = true)
        val input = MissionEnvInput(
            missionId = missionId,
            resources = listOf(),
            missionTypes = listOf(MissionTypeEnum.SEA)
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(missionEntity)
        Mockito.`when`(apiEnvRepo2.patchMission(anyInt(), anyOrNull())).thenReturn(missionEnvEntity)

        // When
        val result = patchMissionEnv.execute(input =  input)
        // Then
        Assertions.assertNotNull(result)
    }


    private fun getMockMissionEntity(
        missionId: Int,
        controlUnitId: Int,
        withoutResource: Boolean? = null
    ): MissionEnvEntity {
        return MissionEnvEntity(
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
            controlUnits = listOf(
                LegacyControlUnitEntity(
                    id = controlUnitId,
                    administration = "fake-admin",
                    isArchived = false,
                    name = "fake-unit",
                    resources = if (withoutResource == true) mutableListOf() else mutableListOf(
                        LegacyControlUnitResourceEntity(
                            id = 1,
                            name = "mock-resource",
                            controlUnitId = controlUnitId
                        )
                    )
                )
            )
        )
    }

    private fun getMissionEnvEntity(missionId: Int, controlUnitId: Int): MissionEnvEntity {
        return MissionEnvEntity(
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
            controlUnits = listOf(
                LegacyControlUnitEntity(
                    id = controlUnitId,
                    administration = "fake-admin",
                    isArchived = false,
                    name = "fake-unit",
                    resources = mutableListOf(
                        LegacyControlUnitResourceEntity(
                            id = 1,
                            name = "mock-resource",
                            controlUnitId = controlUnitId
                        )
                    )
                )
            )
        )
    }

}
