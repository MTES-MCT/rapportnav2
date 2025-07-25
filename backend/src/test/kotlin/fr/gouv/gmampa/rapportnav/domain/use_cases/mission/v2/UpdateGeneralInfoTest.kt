package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.*
import org.assertj.core.api.Assertions.assertThat

@SpringBootTest(classes = [UpdateGeneralInfo::class])
class UpdateGeneralInfoTest {

    @Autowired
    private lateinit var updateGeneralInfo: UpdateGeneralInfo

    @MockitoBean
    private lateinit var generalInfoRepository: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var getMissionCrew: GetMissionCrew

    @MockitoBean
    private lateinit var patchMissionEnv: PatchMissionEnv

    @MockitoBean
    private lateinit var processMissionCrew: ProcessMissionCrew

    @MockitoBean
    private lateinit var patchNavMission: PatchNavMission

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    private fun createMissionGeneralInfoEntityData(
        missionId: Int? = null,
        serviceId: Int?,
        missionIdUUID: UUID? = null
    ): MissionGeneralInfoEntity {
        val entity = MissionGeneralInfoEntity(
            id = 1,
            missionId = missionId,
            serviceId = serviceId,
            missionIdUUID = missionIdUUID
        )
        return entity
    }

        @Test
    fun `execute should update mission service when service ID has changed`() {
        // Given
        val missionId = 123
        val oldServiceId = 456
        val newServiceId = 789
        val missionGeneralInfo = MissionGeneralInfo2(
            missionId = missionId,
            serviceId = newServiceId,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            missionTypes = listOf(),
            observations = "Test observations",
            resources = emptyList()
            )

        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionId, oldServiceId)

        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = updateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verify(getMissionCrew).execute(
            missionId = missionId,
            newServiceId = newServiceId,
            oldServiceId = oldServiceId,
            generalInfo = missionGeneralInfo
        )

        assertEquals(
            MissionGeneralInfoEntity2(data = missionGeneralInfoEntity, crew = emptyList()),
            result
        )
    }

    @Test
    fun `execute should update crew and mission env when service ID remains the same`() {
        // Given
        val missionId = 123
        val serviceId = 456
        val crew = listOf<MissionCrew>(MissionCrew(
            id = 3,
            missionId = missionId,
            agent = Agent(id = 1, firstName = "", lastName = ""),
            role = AgentRole(id = 1, title = ""),
            comment = ""
        ))
        val crewEntity = crew.map { it.toMissionCrewEntity() }

        val missionGeneralInfo  = MissionGeneralInfo2(
            missionId = missionId,
            serviceId = serviceId,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            missionTypes = listOf(),
            observations = "Test observations",
            resources = emptyList(),
            crew = crew
            )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionId, serviceId)
        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)
        `when`(processMissionCrew.execute(anyInt(), anyOrNull())).thenReturn(crewEntity)

        val result = updateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)

        // Verify processMissionCrew was called with the exact missionId and crew
        verify(processMissionCrew).execute(
            anyInt(), anyOrNull()
        )

        val input = MissionEnvInput(
            missionId = missionId,
            startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
            endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
            missionTypes = missionGeneralInfo.missionTypes,
            observationsByUnit = missionGeneralInfo.observations,
            resources = missionGeneralInfoEntity.resources?.map { it },
        )
        verify(patchMissionEnv).execute(input)
        assertEquals(MissionGeneralInfoEntity2(data = missionGeneralInfoEntity, crew = crewEntity), result)
    }

    @Test
    fun `execute should handle null service ID`() {
        // Given
        val missionId = 123
        val missionGeneralInfo = MissionGeneralInfo2(
            missionId = missionId,
            serviceId = null,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            missionTypes = listOf(),
            observations = "Test observations",
            resources = emptyList(),
            crew = emptyList(),
        )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionId, null)

        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = updateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verify(processMissionCrew).execute(missionId, emptyList())

        val input = MissionEnvInput(
            missionId = missionId,
            startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
            endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
            missionTypes = missionGeneralInfo.missionTypes,
            observationsByUnit = missionGeneralInfo.observations,
            resources = missionGeneralInfoEntity.resources?.map { it },
        )
        verify(patchMissionEnv).execute(input)
        assertEquals(MissionGeneralInfoEntity2(data = missionGeneralInfoEntity, crew = emptyList()), result)
    }

    @Test
    fun `execute should return null when an exception occurs`() {
        // Given
        val missionId = 123
        val missionGeneralInfo = MissionGeneralInfo2(
            missionId = missionId,
            serviceId = null,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            missionTypes = listOf(),
            observations = "Test observations",
            resources = emptyList(),
            crew = emptyList(),
        )

        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenThrow(RuntimeException("Test exception"))

        val exception = assertThrows<RuntimeException> {
            updateGeneralInfo.execute(missionId, missionGeneralInfo)
        }

        // Then
        assertThat(exception.message).isEqualTo("Test exception")
        verifyNoInteractions(getMissionCrew)
        verifyNoInteractions(processMissionCrew)
        verifyNoInteractions(patchMissionEnv)
    }


    @Test
    fun `execute should update mission service when service ID has changed for missionId uuid`() {
        // Given
        val missionIdUUID = UUID.randomUUID()
        val oldServiceId = 456
        val newServiceId = 789
        val missionGeneralInfo = MissionGeneralInfo2(
            missionIdUUID = missionIdUUID,
            serviceId = newServiceId,
            startDateTimeUtc = Instant.now(),
            endDateTimeUtc = Instant.now(),
            missionTypes = listOf(),
            observations = "Test observations",
            resources = emptyList(),

            )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionIdUUID = missionIdUUID, serviceId = oldServiceId)

        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionIdUUID)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = updateGeneralInfo.execute(missionIdUUID, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verify(getMissionCrew).execute(
            missionIdUUID = missionIdUUID,
            newServiceId = newServiceId,
            oldServiceId = oldServiceId,
            generalInfo = missionGeneralInfo
        )

        assertEquals(
            MissionGeneralInfoEntity2(data = missionGeneralInfoEntity, crew = emptyList()),
            result
        )
    }

}
