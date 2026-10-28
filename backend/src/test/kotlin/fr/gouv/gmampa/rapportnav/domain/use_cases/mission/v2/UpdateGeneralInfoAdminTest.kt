package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfoAdmin
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.adapters.input.AdminGeneralInfosUpdateInput
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionModelMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [UpdateGeneralInfoAdmin::class])
class UpdateGeneralInfoAdminTest {

    @Autowired
    private lateinit var updateGeneralInfoAdmin: UpdateGeneralInfoAdmin

    @MockitoBean
    private lateinit var repository: IMissionGeneralInfoRepository

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var resourceUsageRepository: IResourceUsageRepository

    @MockitoBean
    private lateinit var getPamShipResource: GetPamShipResource

    @MockitoBean
    private lateinit var getMissionByExternalId: GetMissionByExternalId

    @Test
    fun `should update general info by missionId`() {
        val missionId = 123
        val serviceId = 10
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toMissionGeneralInfoModel()
        val service = ServiceEntityMock.create(id = serviceId, name = "Updated Service")
        val updatedEntity = entity.copy(service = service)
        val updatedModel = updatedEntity.toMissionGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId,
            serviceId = serviceId
        )

        whenever(repository.findByMissionId(missionId)).thenReturn(Optional.of(model))
        whenever(getServiceById.execute(serviceId)).thenReturn(service)
        whenever(repository.save(any())).thenReturn(updatedModel)

        val result = updateGeneralInfoAdmin.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.missionId).isEqualTo(missionId)
        assertThat(result?.service?.id).isEqualTo(serviceId)
    }

    @Test
    fun `should update general info by missionIdUUID`() {
        val missionIdUUID = UUID.randomUUID()
        val serviceId = 10
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID)
        val model = entity.toMissionGeneralInfoModel()
        val service = ServiceEntityMock.create(id = serviceId, name = "Updated Service")
        val updatedEntity = entity.copy(service = service)
        val updatedModel = updatedEntity.toMissionGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionIdUUID = missionIdUUID,
            serviceId = serviceId
        )

        whenever(repository.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(model))
        whenever(getServiceById.execute(serviceId)).thenReturn(service)
        whenever(repository.save(any())).thenReturn(updatedModel)

        val result = updateGeneralInfoAdmin.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.missionIdUUID).isEqualTo(missionIdUUID)
        assertThat(result?.service?.id).isEqualTo(serviceId)
    }

    @Test
    fun `should return null when no missionId or missionIdUUID provided`() {
        val input = AdminGeneralInfosUpdateInput(
            missionId = null,
            missionIdUUID = null
        )

        val result = updateGeneralInfoAdmin.execute(input)

        assertThat(result).isNull()
    }

    @Test
    fun `should update without service when serviceId is null`() {
        val missionId = 123
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toMissionGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId,
            serviceId = null
        )

        whenever(repository.findByMissionId(missionId)).thenReturn(Optional.of(model))
        whenever(getServiceById.execute(null)).thenReturn(null)
        whenever(repository.save(any())).thenReturn(model)

        val result = updateGeneralInfoAdmin.execute(input)

        assertThat(result).isNotNull
        assertThat(result?.missionId).isEqualTo(missionId)
    }

    @Test
    fun `should propagate BackendInternalException from repository findByMissionId`() {
        val missionId = 123
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId
        )

        whenever(repository.findByMissionId(missionId)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            updateGeneralInfoAdmin.execute(input)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }

    @Test
    fun `should propagate BackendInternalException from repository save`() {
        val missionId = 123
        val entity = MissionGeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toMissionGeneralInfoModel()
        val internalException = BackendInternalException(
            message = "Save error",
            originalException = RuntimeException("Database error")
        )

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId
        )

        whenever(repository.findByMissionId(missionId)).thenReturn(Optional.of(model))
        whenever(getServiceById.execute(null)).thenReturn(null)
        whenever(repository.save(any())).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            updateGeneralInfoAdmin.execute(input)
        }
        assertThat(exception.message).isEqualTo("Save error")
    }
    // --- PAM metrics dual-write (Phase 1) ---

    private val pamService = ServiceEntityMock.create(id = 5, serviceType = ServiceTypeEnum.PAM, controlUnits = listOf(10404))
    private val irisShip = LegacyControlUnitResourceEntity(
        id = 582, controlUnitId = 10404, name = "Iris", type = ControlUnitResourceType.PATROL_BOAT
    )

    @Test
    fun `should merge the merged PAM values onto the ship row by missionIdUUID`() {
        val missionIdUUID = UUID.randomUUID()
        val current = MissionGeneralInfoEntityMock.create(
            id = 1,
            missionIdUUID = missionIdUUID,
            service = pamService,
            distanceInNauticalMiles = 10f,
            consumedGOInLiters = 3400f,
            fuelCostsInEuro = 4100f
        )
        whenever(repository.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(current.toMissionGeneralInfoModel()))
        whenever(repository.save(any())).thenAnswer { (it.arguments[0] as MissionGeneralInfoEntity).toMissionGeneralInfoModel() }
        whenever(getPamShipResource.execute(anyOrNull())).thenReturn(irisShip)

        updateGeneralInfoAdmin.execute(
            AdminGeneralInfosUpdateInput(missionIdUUID = missionIdUUID, distanceInNauticalMiles = 12.3f)
        )

        // Unchanged admin fields keep their current values (the `?:` merge), so all three usage values are mirrored.
        // The fuel cost is a mission value: it stays in mission_general_info and is not part of the ship row.
        verify(resourceUsageRepository).mergePamMetrics(
            missionIdUUID,
            582,
            PamResourceMetrics(distanceInNauticalMiles = 12.3, consumedGOInLiters = 3400.0)
        )
    }

    @Test
    fun `should resolve the mission UUID from the mirror row for an Int mission`() {
        val missionId = 123
        val local = MissionModelMock.create()
        val current = MissionGeneralInfoEntityMock.create(
            id = 1, missionId = missionId, missionIdUUID = null, service = pamService, consumedFuelInLiters = 20f
        )
        whenever(repository.findByMissionId(missionId)).thenReturn(Optional.of(current.toMissionGeneralInfoModel()))
        whenever(repository.save(any())).thenReturn(current.toMissionGeneralInfoModel())
        whenever(getPamShipResource.execute(anyOrNull())).thenReturn(irisShip)
        whenever(getMissionByExternalId.execute(missionId.toString())).thenReturn(local)

        updateGeneralInfoAdmin.execute(AdminGeneralInfosUpdateInput(missionId = missionId))

        verify(resourceUsageRepository).mergePamMetrics(local.id, 582, PamResourceMetrics(consumedFuelInLiters = 20.0))
    }

    @Test
    fun `should skip the ship row and still return when the ship can't be resolved`() {
        val missionIdUUID = UUID.randomUUID()
        val current = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID, service = pamService)
        whenever(repository.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(current.toMissionGeneralInfoModel()))
        whenever(repository.save(any())).thenReturn(current.toMissionGeneralInfoModel())
        whenever(getPamShipResource.execute(anyOrNull())).thenReturn(null)

        val result = updateGeneralInfoAdmin.execute(
            AdminGeneralInfosUpdateInput(missionIdUUID = missionIdUUID, distanceInNauticalMiles = 12.3f)
        )

        assertThat(result).isNotNull
        verify(resourceUsageRepository, never()).mergePamMetrics(any(), any(), any())
    }

    @Test
    fun `should still return when merging the ship row fails`() {
        val missionIdUUID = UUID.randomUUID()
        val current = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID, service = pamService)
        whenever(repository.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(current.toMissionGeneralInfoModel()))
        whenever(repository.save(any())).thenReturn(current.toMissionGeneralInfoModel())
        whenever(getPamShipResource.execute(anyOrNull())).thenReturn(irisShip)
        whenever(resourceUsageRepository.mergePamMetrics(any(), any(), any())).thenThrow(BackendInternalException("boom"))

        val result = updateGeneralInfoAdmin.execute(
            AdminGeneralInfosUpdateInput(missionIdUUID = missionIdUUID, distanceInNauticalMiles = 12.3f)
        )

        assertThat(result).isNotNull
    }

    @Test
    fun `should not touch the ship row for a non-PAM service`() {
        val missionIdUUID = UUID.randomUUID()
        val ulam = ServiceEntityMock.create(id = 20, serviceType = ServiceTypeEnum.ULAM, controlUnits = listOf(10176))
        val current = MissionGeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID, service = ulam)
        whenever(repository.findByMissionIdUUID(missionIdUUID)).thenReturn(Optional.of(current.toMissionGeneralInfoModel()))
        whenever(repository.save(any())).thenReturn(current.toMissionGeneralInfoModel())

        updateGeneralInfoAdmin.execute(AdminGeneralInfosUpdateInput(missionIdUUID = missionIdUUID))

        verify(getPamShipResource, never()).execute(anyOrNull())
        verify(resourceUsageRepository, never()).mergePamMetrics(any(), any(), any())
    }
}
