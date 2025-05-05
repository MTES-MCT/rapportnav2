package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateMissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
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

        val mockResourceFromDb = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = 12
        )

        val mockControlUnitFromDb = LegacyControlUnitEntity(
            id = 12,
            administration = "fake-admin",
            isArchived = false,
            name = "fake-unit",
            resources = mutableListOf(mockResourceFromDb)
        )

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
            controlUnits = listOf(mockControlUnitFromDb)
        )

        val mockResourceInput = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = mockControlUnitFromDb.id
        )

        val input = MissionEnvInput(
            missionId = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            resources = listOf(mockResourceInput)
        )

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)

        val value = input.toMissionEnvEntity(mockMissionEntity, controlUnitId = input.resources?.get(0)?.controlUnitId)

        Mockito.`when`(apiEnvRepo2.update(value)).thenReturn(value)


        // When
        val result = updateMissionEnv.execute(input)

        // Then
        Assertions.assertNull(result)
    }

    @Test
    fun `execute should return MissionEnvEntity when MissionEntity are not equal`() {

        val missionId = 123

        val mockResourceFromDb = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = 12
        )


        val mockControlUnitFromDb = LegacyControlUnitEntity(
            id = 12,
            administration = "fake-admin",
            isArchived = false,
            name = "fake-unit",
            resources = mutableListOf(mockResourceFromDb)
        )

        val mockResourceInput = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = mockControlUnitFromDb.id
        )

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
            controlUnits = listOf(mockControlUnitFromDb)
        )

        val input = MissionEnvInput(
            missionId = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            observationsByUnit = "observation updated",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            resources = listOf(mockResourceInput)
        )

        val value = input.toMissionEnvEntity(mockMissionEntity)

        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)
        Mockito.`when`(apiEnvRepo2.update(value)).thenReturn(value)


        // When
        val result = updateMissionEnv.execute(input)

        // Then
        Assertions.assertNotNull(result)
        Assertions.assertEquals(input.observationsByUnit, value.observationsByUnit)
    }

    @Test
    fun `execute should return MissionEnvEntity when MissionEntity are not equal as we updated resources`() {

        val missionId = 123

        val mockResourceFromDb = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = 12
        )

        val mockControlUnitFromDb = LegacyControlUnitEntity(
            id = 12,
            administration = "fake-admin",
            isArchived = false,
            name = "fake-unit",
            resources = mutableListOf(mockResourceFromDb)
        )

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
            controlUnits = listOf(mockControlUnitFromDb)
        )

        val mockResourceInput = LegacyControlUnitResourceEntity(
            id = 1,
            name = "mock-resource",
            controlUnitId = mockControlUnitFromDb.id
        )

        val mockResourceInput2 = LegacyControlUnitResourceEntity(
            id = 2,
            name = "mock-resource-2",
            controlUnitId = mockControlUnitFromDb.id
        )

        val input = MissionEnvInput(
            missionId = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            observationsByUnit = "observation",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            resources = listOf(mockResourceInput, mockResourceInput2)
        )


        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)

        val value = input.toMissionEnvEntity(mockMissionEntity, controlUnitId = input.resources?.get(0)?.controlUnitId)
        Mockito.`when`(apiEnvRepo2.update(value)).thenReturn(value)


        // When
        val result = updateMissionEnv.execute(input)

        val firstControlUnit = value.controlUnits?.firstOrNull()
        val firstResource = firstControlUnit?.resources?.firstOrNull()

        val secondResource = firstControlUnit?.resources?.get(1)

        // Then
        Assertions.assertNotNull(result)
        Assertions.assertNotNull(input.resources?.firstOrNull())
        Assertions.assertEquals(input.resources?.firstOrNull()?.id, firstResource?.id)
        Assertions.assertEquals(input.resources?.get(1)?.id, secondResource?.id)
        Assertions.assertEquals(1, mockMissionEntity.controlUnits[0].resources?.size)
    }

    @Test
    fun `execute should not throw Exception when resources are empty`() {

        val missionId = 123



        val mockControlUnitFromDb = LegacyControlUnitEntity(
            id = 12,
            administration = "fake-admin",
            isArchived = false,
            name = "fake-unit",
            resources = mutableListOf()
        )

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
            controlUnits = listOf(mockControlUnitFromDb)
        )

        val input = MissionEnvInput(
            missionId = missionId,
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2019-09-08T21:00:00.000+01:00"),
            observationsByUnit = "observation updated",
            endDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00"),
            resources = listOf()
        )


        // Mock behavior of getEnvMissionById2 to return a MissionEntity
        Mockito.`when`(getEnvMissionById2.execute(missionId)).thenReturn(mockMissionEntity)

        val value = input.toMissionEnvEntity(mockMissionEntity)
        Mockito.`when`(apiEnvRepo2.update(value)).thenReturn(value)


        // When
        val result = updateMissionEnv.execute(input)

        // Then
        Assertions.assertNotNull(result)
    }

}
