package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.CreateOrUpdateGeneralInfo
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetGeneralInfo2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessMissionCrew
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateMissionEnv
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateMissionService2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.Agent
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.AgentRole
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.MissionCrew
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import org.mockito.Mockito.`when`
import org.mockito.Mockito.*

@SpringBootTest(classes = [CreateOrUpdateGeneralInfo::class])
class CreateOrUpdateGeneralInfoTest {

    @Autowired
    private lateinit var createOrUpdateGeneralInfo: CreateOrUpdateGeneralInfo

    @MockitoBean
    private lateinit var generalInfoRepository: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var updateMissionService2: UpdateMissionService2

    @MockitoBean
    private lateinit var getGeneralInfo2: GetGeneralInfo2

    @MockitoBean
    private lateinit var updateMissionEnv: UpdateMissionEnv

    @MockitoBean
    private lateinit var processMissionCrew: ProcessMissionCrew

    private fun createMissionGeneralInfoEntityData(missionId: Int, serviceId: Int?): MissionGeneralInfoEntity {
        val entity = MissionGeneralInfoEntity(
            id = 1,
            missionId = missionId,
            serviceId = serviceId
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
            resources = emptyList(),

            )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = MissionGeneralInfoEntity2(
            data = createMissionGeneralInfoEntityData(missionId, oldServiceId)
        )

        // When
        `when`(getGeneralInfo2.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = createOrUpdateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verify(updateMissionService2).execute(missionId=missionId, serviceId = newServiceId)
        verifyNoInteractions(processMissionCrew)
        verifyNoInteractions(updateMissionEnv)

        assertEquals(
            MissionGeneralInfoEntity2(data = missionGeneralInfoEntity),
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
        val previousEntity = MissionGeneralInfoEntity2(
            data = createMissionGeneralInfoEntityData(missionId, serviceId)
        )
        // When
        `when`(getGeneralInfo2.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = createOrUpdateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verifyNoInteractions(updateMissionService2)

        // Verify processMissionCrew was called with the exact missionId and crew
        verify(processMissionCrew).execute(missionId, missionGeneralInfo.crew.orEmpty())

        assertEquals(MissionGeneralInfoEntity2(data = missionGeneralInfoEntity), result)
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
        val previousEntity = MissionGeneralInfoEntity2(
            data = createMissionGeneralInfoEntityData(missionId, null)
        )

        // When
        `when`(getGeneralInfo2.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)

        val result = createOrUpdateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)
        verifyNoInteractions(updateMissionService2)
        verify(processMissionCrew).execute(missionId, emptyList())

        // TODO check that one
        // verify(updateMissionEnv).execute(any())

        assertEquals(MissionGeneralInfoEntity2(data = missionGeneralInfoEntity), result)
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
        `when`(getGeneralInfo2.execute(missionId)).thenThrow(RuntimeException("Test exception"))

        val result = createOrUpdateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        assertNull(result)
        verifyNoInteractions(updateMissionService2)
        verifyNoInteractions(processMissionCrew)
        verifyNoInteractions(updateMissionEnv)
    }

}
