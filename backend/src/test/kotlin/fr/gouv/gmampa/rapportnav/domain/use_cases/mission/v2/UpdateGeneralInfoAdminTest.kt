package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.UpdateGeneralInfoAdmin
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input.AdminGeneralInfosUpdateInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.admin.input.AdminGeneralInfosUpdateServiceInput
import fr.gouv.gmampa.rapportnav.mocks.mission.GeneralInfoEntityMock
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.any
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
    private lateinit var repository: IGeneralInfoRepository

    @MockitoBean
    private lateinit var getServiceById: GetServiceById

    @Test
    fun `should update general info by missionId`() {
        val missionId = 123
        val serviceId = 10
        val entity = GeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toGeneralInfoModel()
        val service = ServiceEntityMock.create(id = serviceId, name = "Updated Service")
        val updatedEntity = entity.copy(service = service)
        val updatedModel = updatedEntity.toGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId,
            service = AdminGeneralInfosUpdateServiceInput(id = serviceId)
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
        val entity = GeneralInfoEntityMock.create(id = 1, missionIdUUID = missionIdUUID)
        val model = entity.toGeneralInfoModel()
        val service = ServiceEntityMock.create(id = serviceId, name = "Updated Service")
        val updatedEntity = entity.copy(service = service)
        val updatedModel = updatedEntity.toGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionIdUUID = missionIdUUID,
            service = AdminGeneralInfosUpdateServiceInput(id = serviceId)
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
    fun `should update without service when service input is null`() {
        val missionId = 123
        val entity = GeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toGeneralInfoModel()

        val input = AdminGeneralInfosUpdateInput(
            missionId = missionId,
            service = null
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
        val entity = GeneralInfoEntityMock.create(id = 1, missionId = missionId)
        val model = entity.toGeneralInfoModel()
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
}
