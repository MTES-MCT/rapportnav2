package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServices
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetServices::class])
@ContextConfiguration(classes = [GetServices::class])
class GetServicesTest {

    @Autowired
    private lateinit var getServices: GetServices

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    @Test
    fun `should return all services`() {
        val service1 = ServiceEntityMock.create(id = 1, name = "Service 1")
        val service2 = ServiceEntityMock.create(id = 2, name = "Service 2")

        `when`(serviceRepository.findAll()).thenReturn(listOf(
            service1.toServiceModel(),
            service2.toServiceModel()
        ))

        val useCase = GetServices(serviceRepository)
        val result = useCase.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Service 1")
        assertThat(result[1].name).isEqualTo("Service 2")
    }

    @Test
    fun `should return empty list when no services`() {
        `when`(serviceRepository.findAll()).thenReturn(emptyList())

        val useCase = GetServices(serviceRepository)
        val result = useCase.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Database error")
        )

        `when`(serviceRepository.findAll()).thenAnswer { throw internalException }

        val useCase = GetServices(serviceRepository)

        val exception = assertThrows<BackendInternalException> {
            useCase.execute()
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
