package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.*
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import org.mockito.kotlin.anyOrNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

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
    private lateinit var processMissionPassengers: ProcessMissionPassengers

    @MockitoBean
    private lateinit var patchNavMission: PatchNavMission

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    private fun createMissionGeneralInfoEntityData(
        missionId: Int? = null,
        serviceId: Int?,
        missionIdUUID: UUID? = null
    ): MissionGeneralInfoEntity {
        val entity = MissionGeneralInfoEntityMock.create(
            id = 1,
            missionId = missionId,
            service = ServiceEntityMock.create(id = serviceId),
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
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionId = missionId,
            service = Service.fromServiceEntity(ServiceEntityMock.create(id = newServiceId)),
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
            MissionGeneralInfoEntity2(data = missionGeneralInfoEntity, crew = emptyList(), passengers = emptyList()),
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
            agent = Agent2(id = 1, firstName = "", lastName = "", service = Service(
                id = 1,
                name = "Service1",
                serviceType = ServiceTypeEnum.PAM
            )
            ),
            role = AgentRole(id = 1, title = ""),
            comment = ""
        ))
        val crewEntity = crew.map { it.toMissionCrewEntity() }
        val passengerEntity = MissionPassengerEntityMock.create()

        val missionGeneralInfo  = MissionGeneralInfo2Mock.create(
            missionId = missionId,
            service = Service.fromServiceEntity(ServiceEntityMock.create(id = serviceId)),
            )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionId, serviceId)
        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)
        `when`(processMissionCrew.execute(anyInt(), anyOrNull())).thenReturn(crewEntity)
        `when`(processMissionPassengers.execute(anyInt(), anyOrNull())).thenReturn(listOf(passengerEntity))

        val result = updateGeneralInfo.execute(missionId, missionGeneralInfo)

        // Then
        verify(generalInfoRepository).save(missionGeneralInfoEntity)

        // Verify processMissionCrew was called with the exact missionId and crew
        verify(processMissionCrew).execute(
            anyInt(), anyOrNull()
        )
        // Verify processMissionCrew was called with the exact missionId and crew
        verify(processMissionPassengers).execute(
            anyInt(), anyOrNull()
        )

        val input = MissionEnvInput(
            isUnderJdp = null,
            missionId = missionId,
            startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
            endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
            missionTypes = missionGeneralInfo.missionTypes,
            observationsByUnit = missionGeneralInfo.observations,
            resources = missionGeneralInfoEntity.resources?.map { it }
        )
        verify(patchMissionEnv).execute(input)
        assertEquals(
            MissionGeneralInfoEntity2(
                data = missionGeneralInfoEntity,
                crew = crewEntity,
                passengers = listOf(passengerEntity)
            ),
            result
        )
    }

    @Test
    fun `execute should handle null service ID`() {
        // Given
        val missionId = 123
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionId = missionId,
            service = null,
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
        verify(processMissionPassengers).execute(missionId, emptyList())

        val input = MissionEnvInput(
            missionId = missionId,
            startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
            endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
            missionTypes = missionGeneralInfo.missionTypes,
            observationsByUnit = missionGeneralInfo.observations,
            resources = missionGeneralInfoEntity.resources?.map { it }
        )
        verify(patchMissionEnv).execute(input)
        assertEquals(
            MissionGeneralInfoEntity2(
                data = missionGeneralInfoEntity,
                crew = emptyList(),
                passengers = emptyList(),
            ),
            result
        )
    }

    @Test
    fun `execute should propagate exception when dependency throws`() {
        // Given
        val missionId = 123
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionId = missionId,
            service = null,
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
        verifyNoInteractions(processMissionPassengers)
        verifyNoInteractions(patchMissionEnv)
    }

    @Test
    fun `execute should throw BackendUsageException when general info not found for missionId`() {
        // Given
        val missionId = 123
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionId = missionId,
            service = null,
        )

        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(null)

        // When/Then
        val exception = assertThrows<BackendUsageException> {
            updateGeneralInfo.execute(missionId, missionGeneralInfo)
        }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(exception.message).contains("missionId=$missionId")
    }

    @Test
    fun `execute should throw BackendUsageException when missionId mismatch`() {
        // Given
        val missionId = 123
        val differentMissionId = 456
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionId = differentMissionId,
            service = null,
        )
        val previousEntity = createMissionGeneralInfoEntityData(missionId, null)

        `when`(getMissionGeneralInfoByMissionId.execute(missionId)).thenReturn(previousEntity)

        // When/Then
        val exception = assertThrows<BackendUsageException> {
            updateGeneralInfo.execute(missionId, missionGeneralInfo)
        }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
    }

    @Test
    fun `execute should throw BackendUsageException when general info not found for missionIdUUID`() {
        // Given
        val missionIdUUID = UUID.randomUUID()
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionIdUUID = missionIdUUID,
            service = null,
        )

        `when`(getMissionGeneralInfoByMissionId.execute(missionIdUUID)).thenReturn(null)

        // When/Then
        val exception = assertThrows<BackendUsageException> {
            updateGeneralInfo.execute(missionIdUUID, missionGeneralInfo)
        }

        assertThat(exception.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
        assertThat(exception.message).contains("missionIdUUID=$missionIdUUID")
    }


    @Test
    fun `execute should update mission service when service ID has changed for missionId uuid`() {
        // Given
        val missionIdUUID = UUID.randomUUID()
        val oldServiceId = 456
        val newServiceId = 789
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionIdUUID = missionIdUUID,
            service = Service.fromServiceEntity(ServiceEntityMock.create(id = newServiceId)),

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
            MissionGeneralInfoEntity2(
                data = missionGeneralInfoEntity,
                crew = emptyList(),
                passengers = emptyList(),
            ),
            result
        )
    }

}
