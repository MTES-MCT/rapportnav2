package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.service.ServiceTypeEnum
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.ServiceModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.*

@SpringBootTest(classes = [GetServiceById::class])
@ContextConfiguration(classes = [GetServiceById::class])
class GetServiceByIdTest {

    @Autowired
    private lateinit var getServiceById: GetServiceById

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    @Test
    fun `should return null when id is null`() {
        val result = getServiceById.execute(null)

        assertThat(result).isNull()
        verify(serviceRepository, never()).findById(any())
    }

    @Test
    fun `should return service entity when found`() {
        val serviceId = 10
        val serviceModel = ServiceModel(
            id = serviceId,
            name = "Test Service",
            serviceType = ServiceTypeEnum.PAM,
            controlUnits = listOf(100, 200)
        )

        `when`(serviceRepository.findById(serviceId)).thenReturn(Optional.of(serviceModel))

        val result = getServiceById.execute(serviceId)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(serviceId)
        assertThat(result?.name).isEqualTo("Test Service")
        assertThat(result?.serviceType).isEqualTo(ServiceTypeEnum.PAM)
        assertThat(result?.controlUnits).containsExactly(100, 200)
    }

    @Test
    fun `should return null when service not found`() {
        val serviceId = 999

        `when`(serviceRepository.findById(serviceId)).thenReturn(Optional.empty())

        val result = getServiceById.execute(serviceId)

        assertThat(result).isNull()
    }

    @Test
    fun `should throw BackendInternalException when repository fails`() {
        val serviceId = 10

        `when`(serviceRepository.findById(serviceId)).thenAnswer { throw RuntimeException("Database error") }

        val exception = assertThrows<BackendInternalException> {
            getServiceById.execute(serviceId)
        }
        assertThat(exception.message).contains("GetServiceById failed for serviceId=$serviceId")
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val serviceId = 10
        val originalException = BackendInternalException(
            message = "Original error",
            originalException = RuntimeException("Cause")
        )

        `when`(serviceRepository.findById(serviceId)).thenAnswer { throw originalException }

        val exception = assertThrows<BackendInternalException> {
            getServiceById.execute(serviceId)
        }
        assertThat(exception.message).isEqualTo("Original error")
    }
}
