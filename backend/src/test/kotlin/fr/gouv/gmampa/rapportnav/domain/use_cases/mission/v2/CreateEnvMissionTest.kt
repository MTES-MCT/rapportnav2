package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnit.IEnvControlUnitRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.CreateEnvMission
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant

@SpringBootTest(classes = [CreateEnvMission::class])
class CreateEnvMissionTest {

    @MockitoBean
    private lateinit var monitorEnvRepo: IEnvMissionRepository

    @MockitoBean
    private lateinit var monitorEnvControlUnitRepo: IEnvControlUnitRepository

    @Autowired
    private lateinit var createEnvMission: CreateEnvMission

    @Test
    fun `should throw exception when control units is empty`() {
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf())
        val exception = assertThrows<BackendUsageException> {
            createEnvMission.getControlUnits(controlUnitIds = listOf())
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
        assertEquals("CreateEnvMission: controlUnits is empty for this user", exception.message)
    }

    @Test
    fun `should throw exception when no control units match the provided ids`() {
        val controlUnit = LegacyControlUnitEntity(
            id = 1,
            isArchived = false,
            name = "control unit 1"
        )
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf(controlUnit))

        val exception = assertThrows<BackendUsageException> {
            createEnvMission.getControlUnits(controlUnitIds = listOf(99, 100))
        }

        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
    }

    @Test
    fun `should remove resources from control units`() {
        val controlUnit = LegacyControlUnitEntity(
            id = 1,
            isArchived = true,
            name = "control unit 1",
            resources = mutableListOf(
                LegacyControlUnitResourceEntity(
                    controlUnitId = 1,
                    name = "resource1",
                    id = 11
                )
            )
        )
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf(controlUnit))
        val response = createEnvMission.getControlUnits(controlUnitIds = listOf(1))

        assertThat(response).isNotNull
        assertThat(response[0].id).isEqualTo(controlUnit.id)
        assertThat(response[0].name).isEqualTo(controlUnit.name)
        assertThat(response[0].isArchived).isEqualTo(controlUnit.isArchived)
        assertThat(response[0].resources).isEmpty()
    }

    @Test
    fun `should filter and return only matching control units`() {
        val controlUnit1 = LegacyControlUnitEntity(id = 1, isArchived = false, name = "Unit 1")
        val controlUnit2 = LegacyControlUnitEntity(id = 2, isArchived = false, name = "Unit 2")
        val controlUnit3 = LegacyControlUnitEntity(id = 3, isArchived = false, name = "Unit 3")

        Mockito.`when`(monitorEnvControlUnitRepo.findAll())
            .thenReturn(listOf(controlUnit1, controlUnit2, controlUnit3))

        val response = createEnvMission.getControlUnits(controlUnitIds = listOf(1, 3))

        assertThat(response).hasSize(2)
        assertThat(response.map { it.id }).containsExactlyInAnyOrder(1, 3)
    }

    @Test
    fun `should execute and return MissionEnvEntity`() {
        val controlUnit = LegacyControlUnitEntity(id = 1, isArchived = false, name = "Unit 1")
        val generalInfo = MissionGeneralInfo2Mock.create(
            missionTypes = listOf(MissionTypeEnum.SEA),
            startDateTimeUtc = Instant.parse("2025-05-01T08:00:00Z"),
            endDateTimeUtc = Instant.parse("2025-05-01T18:00:00Z")
        )

        val expectedMissionEnv = MissionEnvEntity(
            id = 123,
            missionTypes = listOf(MissionTypeEnum.SEA),
            controlUnits = listOf(controlUnit),
            startDateTimeUtc = generalInfo.startDateTimeUtc!!,
            endDateTimeUtc = generalInfo.endDateTimeUtc,
            missionSource = MissionSourceEnum.RAPPORT_NAV,
            isDeleted = false,
            hasMissionOrder = false,
            isUnderJdp = false,
            isGeometryComputedFromControls = false
        )

        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf(controlUnit))
        Mockito.`when`(monitorEnvRepo.createMission(anyOrNull())).thenReturn(expectedMissionEnv)

        val result = createEnvMission.execute(generalInfo, controlUnitIds = listOf(1))

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(123)
        assertThat(result.missionSource).isEqualTo(MissionSourceEnum.RAPPORT_NAV)
        Mockito.verify(monitorEnvRepo, Mockito.times(1)).createMission(anyOrNull())
    }

    @Test
    fun `should throw BackendInternalException when repository returns null`() {
        val controlUnit = LegacyControlUnitEntity(id = 1, isArchived = false, name = "Unit 1")
        val generalInfo = MissionGeneralInfo2Mock.create()

        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf(controlUnit))
        Mockito.`when`(monitorEnvRepo.createMission(anyOrNull())).thenReturn(null)

        val exception = assertThrows<BackendInternalException> {
            createEnvMission.execute(generalInfo, controlUnitIds = listOf(1))
        }

        assertEquals("CreateEnvMission: Failed to create mission in MonitorEnv", exception.message)
    }

    @Test
    fun `should use empty list when controlUnitIds is null`() {
        Mockito.`when`(monitorEnvControlUnitRepo.findAll()).thenReturn(listOf())
        val generalInfo = MissionGeneralInfo2Mock.create()

        val exception = assertThrows<BackendUsageException> {
            createEnvMission.execute(generalInfo, controlUnitIds = null)
        }

        assertEquals(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION, exception.code)
    }
}
