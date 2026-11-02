package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.GetEnvMissionById
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.*
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.passenger.ProcessMissionPassengers
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.MissionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.crew.*
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import fr.gouv.gmampa.rapportnav.mocks.mission.EnvMissionMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfo2Mock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.passenger.MissionPassengerEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
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
    private lateinit var getMissionByExternalId: GetMissionByExternalId

    @MockitoBean
    private lateinit var getMissionExternalId: GetMissionExternalId

    @MockitoBean
    private lateinit var getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId

    @MockitoBean
    private lateinit var entityValidityValidator: EntityValidityValidator

    @MockitoBean
    private lateinit var resourceUsageRepository: IResourceUsageRepository

    @MockitoBean
    private lateinit var getEnvMissionById: GetEnvMissionById

    @MockitoBean
    private lateinit var syncLocalMissionWithMonitorEnv: SyncLocalMissionWithMonitorEnv

    @MockitoBean
    private lateinit var recomputeMissionValidation: RecomputeMissionValidation

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var getPamShipResource: GetPamShipResource

    @BeforeEach
    fun setup() {
        // Default: validation passes
        `when`(entityValidityValidator.validate(anyOrNull(), anyOrNull())).thenReturn(
            CompletenessForStatsEntity(
                status = CompletenessForStatsStatusEnum.VALID,
                errors = emptyList()
            )
        )
        // Default: no local mission row to sync (Int-overload write-sync path)
        `when`(getMissionByExternalId.execute(anyString())).thenReturn(null)
        // Default: UUID maps to a nav-only mission (no MonitorEnv mirror)
        `when`(getMissionExternalId.execute(anyOrNull())).thenReturn(null)
    }

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
            agent = Agent(id = 1, firstName = "", lastName = "", service = Service(
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

    @Test
    fun `execute by UUID should propagate to MonitorEnv when the mission has an external id`() {
        // Given
        val missionIdUUID = UUID.randomUUID()
        val externalId = 456
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(
            missionIdUUID = missionIdUUID,
            service = null,
        )
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID)
        val missionGeneralInfoModel = missionGeneralInfoEntity.toMissionGeneralInfoModel()
        val previousEntity = createMissionGeneralInfoEntityData(missionIdUUID = missionIdUUID, serviceId = null)

        // When
        `when`(getMissionGeneralInfoByMissionId.execute(missionIdUUID)).thenReturn(previousEntity)
        `when`(generalInfoRepository.save(missionGeneralInfoEntity)).thenReturn(missionGeneralInfoModel)
        `when`(getMissionExternalId.execute(missionIdUUID)).thenReturn(externalId)

        updateGeneralInfo.execute(missionIdUUID, missionGeneralInfo)

        // Then
        val input = MissionEnvInput(
            missionId = externalId,
            isUnderJdp = missionGeneralInfo.isUnderJdp,
            startDateTimeUtc = missionGeneralInfo.startDateTimeUtc,
            endDateTimeUtc = missionGeneralInfo.endDateTimeUtc,
            missionTypes = missionGeneralInfo.missionTypes,
            observationsByUnit = missionGeneralInfo.observations,
            resources = missionGeneralInfoEntity.resources?.map { it }
        )
        verify(patchMissionEnv).execute(input)
        verify(recomputeMissionValidation).forEnvMission(externalId)
    }

    @Test
    fun `execute by UUID should recompute the nav mission validation when there is no MonitorEnv mirror`() {
        val missionIdUUID = UUID.randomUUID()
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(missionIdUUID = missionIdUUID, service = null)
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID)
        `when`(getMissionGeneralInfoByMissionId.execute(missionIdUUID))
            .thenReturn(createMissionGeneralInfoEntityData(missionIdUUID = missionIdUUID, serviceId = null))
        `when`(generalInfoRepository.save(missionGeneralInfoEntity))
            .thenReturn(missionGeneralInfoEntity.toMissionGeneralInfoModel())

        updateGeneralInfo.execute(missionIdUUID, missionGeneralInfo)

        verify(recomputeMissionValidation).forNavMission(missionIdUUID)
        verify(recomputeMissionValidation, never()).forEnvMission(anyOrNull())
    }

    @Test
    fun `execute by Int should persist usage before patching MonitorEnv, then recompute the env mission`() {
        val missionId = 123
        val local = MissionModelMock.create()
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(missionId = missionId)
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        `when`(getMissionGeneralInfoByMissionId.execute(missionId))
            .thenReturn(createMissionGeneralInfoEntityData(missionId, serviceId = null))
        `when`(generalInfoRepository.save(missionGeneralInfoEntity))
            .thenReturn(missionGeneralInfoEntity.toMissionGeneralInfoModel())
        `when`(getMissionByExternalId.execute(missionId.toString())).thenReturn(local)

        updateGeneralInfo.execute(missionId, missionGeneralInfo)

        val order = inOrder(resourceUsageRepository, patchMissionEnv, recomputeMissionValidation)
        order.verify(resourceUsageRepository).replaceForMission(local.id, emptyList())
        order.verify(patchMissionEnv).execute(anyOrNull())
        order.verify(recomputeMissionValidation).forEnvMission(missionId)
    }

    @Test
    fun `execute by Int should create the local mirror row when missing so usage is not dropped`() {
        val missionId = 123
        val created = MissionModelMock.create()
        val envMission = EnvMissionMock.create(id = missionId)
        val missionGeneralInfo = MissionGeneralInfo2Mock.create(missionId = missionId)
        val missionGeneralInfoEntity = missionGeneralInfo.toMissionGeneralInfoEntity(missionId)
        `when`(getMissionGeneralInfoByMissionId.execute(missionId))
            .thenReturn(createMissionGeneralInfoEntityData(missionId, serviceId = null))
        `when`(generalInfoRepository.save(missionGeneralInfoEntity))
            .thenReturn(missionGeneralInfoEntity.toMissionGeneralInfoModel())
        `when`(getEnvMissionById.execute(missionId)).thenReturn(envMission)
        `when`(syncLocalMissionWithMonitorEnv.execute(missionId, envMission)).thenReturn(created)

        updateGeneralInfo.execute(missionId, missionGeneralInfo)

        verify(resourceUsageRepository).replaceForMission(created.id, emptyList())
    }

    // --- PAM metrics dual-write (Phase 1) ---

    private val pamService = ServiceEntityMock.create(id = 5, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10404))
    private val irisShip = LegacyControlUnitResourceEntity(
        id = 582, controlUnitId = 10404, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT
    )

    private fun pamGeneralInfo(missionId: Int? = 0, missionIdUUID: UUID? = null): MissionGeneralInfo2 =
        MissionGeneralInfo2Mock.create(
            missionId = missionId,
            missionIdUUID = missionIdUUID,
            service = Service.fromServiceEntity(pamService)
        ).copy(
            distanceInNauticalMiles = 12.3f,
            consumedGOInLiters = 3400f,
            consumedFuelInLiters = 20f,
            // Mission costs: saved in mission_general_info, never copied to the ship row.
            operatingCostsInEuro = 900f,
            fuelCostsInEuro = 4100f
        )

    private fun shipRow(missionIdUUID: UUID) = ResourceUsageEntity(
        missionIdUUID = missionIdUUID,
        resourceId = 582,
        distanceInNauticalMiles = 12.3,
        consumedGOInLiters = 3400.0,
        consumedFuelInLiters = 20.0
    )

    private fun givenIntMission(missionId: Int, generalInfo: MissionGeneralInfo2, serviceId: Int? = pamService.id) =
        MissionModelMock.create().also { local ->
            val entity = generalInfo.toMissionGeneralInfoEntity(missionId)
            `when`(getMissionGeneralInfoByMissionId.execute(missionId))
                .thenReturn(createMissionGeneralInfoEntityData(missionId, serviceId = serviceId))
            `when`(generalInfoRepository.save(entity)).thenReturn(entity.toMissionGeneralInfoModel())
            `when`(getMissionByExternalId.execute(missionId.toString())).thenReturn(local)
        }

    private fun givenUuidMission(missionIdUUID: UUID, generalInfo: MissionGeneralInfo2) {
        val entity = generalInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID)
        `when`(getMissionGeneralInfoByMissionId.execute(missionIdUUID))
            .thenReturn(createMissionGeneralInfoEntityData(missionIdUUID = missionIdUUID, serviceId = pamService.id))
        `when`(generalInfoRepository.save(entity)).thenReturn(entity.toMissionGeneralInfoModel())
    }

    @Test
    fun `execute by Int writes the PAM metrics on the ship row alongside mission_general_info`() {
        val missionId = 123
        val generalInfo = pamGeneralInfo(missionId = missionId)
        val local = givenIntMission(missionId, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenReturn(pamService)
        `when`(getPamShipResource.execute(pamService)).thenReturn(irisShip)

        updateGeneralInfo.execute(missionId, generalInfo)

        verify(generalInfoRepository).save(generalInfo.toMissionGeneralInfoEntity(missionId))
        verify(resourceUsageRepository).replaceForMission(local.id, listOf(shipRow(local.id)))
    }

    @Test
    fun `execute by Int still saves when the PAM ship can't be resolved`() {
        val missionId = 123
        val generalInfo = pamGeneralInfo(missionId = missionId)
        val local = givenIntMission(missionId, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenReturn(pamService)
        `when`(getPamShipResource.execute(pamService)).thenReturn(null)

        updateGeneralInfo.execute(missionId, generalInfo)

        verify(generalInfoRepository).save(generalInfo.toMissionGeneralInfoEntity(missionId))
        verify(resourceUsageRepository).replaceForMission(local.id, emptyList())
        verify(patchMissionEnv).execute(anyOrNull())
        verify(recomputeMissionValidation).forEnvMission(missionId)
    }

    @Test
    fun `execute by Int still saves when the service lookup fails`() {
        val missionId = 123
        val generalInfo = pamGeneralInfo(missionId = missionId)
        val local = givenIntMission(missionId, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenThrow(RuntimeException("boom"))

        updateGeneralInfo.execute(missionId, generalInfo)

        verify(resourceUsageRepository).replaceForMission(local.id, emptyList())
        verify(patchMissionEnv).execute(anyOrNull())
    }

    @Test
    fun `execute by Int does not look for a ship for a ULAM service`() {
        val missionId = 123
        val ulamService = ServiceEntityMock.create(id = 20, serviceType = ServiceTypeEnum.ULAM, controlUnits = listOf(10176))
        val generalInfo = pamGeneralInfo(missionId = missionId).copy(service = Service.fromServiceEntity(ulamService))
        val local = givenIntMission(missionId, generalInfo, serviceId = ulamService.id)
        `when`(getServiceById.execute(ulamService.id)).thenReturn(ulamService)

        updateGeneralInfo.execute(missionId, generalInfo)

        verify(getPamShipResource, never()).execute(anyOrNull())
        verify(resourceUsageRepository).replaceForMission(local.id, emptyList())
    }

    @Test
    fun `execute resolves the ship from the stored service, not the payload's control units`() {
        val missionId = 123
        val tampered = pamService.copy(controlUnits = listOf(99999))
        val generalInfo = pamGeneralInfo(missionId = missionId).copy(service = Service.fromServiceEntity(tampered))
        givenIntMission(missionId, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenReturn(pamService)
        `when`(getPamShipResource.execute(pamService)).thenReturn(irisShip)

        updateGeneralInfo.execute(missionId, generalInfo)

        verify(getPamShipResource).execute(pamService)
        verify(getPamShipResource, never()).execute(tampered)
    }

    @Test
    fun `execute by UUID writes the PAM metrics on the ship row`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfo = pamGeneralInfo(missionIdUUID = missionIdUUID)
        givenUuidMission(missionIdUUID, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenReturn(pamService)
        `when`(getPamShipResource.execute(pamService)).thenReturn(irisShip)

        updateGeneralInfo.execute(missionIdUUID, generalInfo)

        verify(resourceUsageRepository).replaceForMission(missionIdUUID, listOf(shipRow(missionIdUUID)))
    }

    @Test
    fun `execute by UUID still saves when the PAM ship can't be resolved`() {
        val missionIdUUID = UUID.randomUUID()
        val generalInfo = pamGeneralInfo(missionIdUUID = missionIdUUID)
        givenUuidMission(missionIdUUID, generalInfo)
        `when`(getServiceById.execute(pamService.id)).thenReturn(pamService)
        `when`(getPamShipResource.execute(pamService)).thenReturn(null)

        updateGeneralInfo.execute(missionIdUUID, generalInfo)

        verify(generalInfoRepository).save(generalInfo.toMissionGeneralInfoEntity(missionIdUUID = missionIdUUID))
        verify(resourceUsageRepository).replaceForMission(missionIdUUID, emptyList())
        verify(recomputeMissionValidation).forNavMission(missionIdUUID)
    }
}
