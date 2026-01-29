package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.DeleteService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.kotlin.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [DeleteService::class])
@ContextConfiguration(classes = [DeleteService::class])
class DeleteServiceTest {

    @Autowired
    private lateinit var deleteService: DeleteService

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    @Test
    fun `should call repository deleteById`() {
        val serviceId = 1

        doNothing().`when`(serviceRepository).deleteById(serviceId)

        deleteService.execute(serviceId)

        verify(serviceRepository).deleteById(serviceId)
    }

    @Test
    fun `should propagate BackendUsageException when service not found`() {
        val serviceId = 999
        val exception = BackendUsageException(
            code = BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION,
            message = "Service not found"
        )

        `when`(serviceRepository.deleteById(serviceId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendUsageException> {
            deleteService.execute(serviceId)
        }
        assertThat(thrown.code).isEqualTo(BackendUsageErrorCode.COULD_NOT_FIND_EXCEPTION)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val serviceId = 1
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(serviceRepository.deleteById(serviceId)).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            deleteService.execute(serviceId)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
