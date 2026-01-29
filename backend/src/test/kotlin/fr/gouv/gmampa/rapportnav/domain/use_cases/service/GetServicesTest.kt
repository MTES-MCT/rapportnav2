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
    fun `should return list of services`() {
        val serviceModels = listOf(
            ServiceEntityMock.create(id = 1, name = "Service A").toServiceModel(),
            ServiceEntityMock.create(id = 2, name = "Service B").toServiceModel()
        )

        `when`(serviceRepository.findAll()).thenReturn(serviceModels)

        val result = getServices.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].name).isEqualTo("Service A")
        assertThat(result[1].name).isEqualTo("Service B")
    }

    @Test
    fun `should return empty list when no services`() {
        `when`(serviceRepository.findAll()).thenReturn(emptyList())

        val result = getServices.execute()

        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(serviceRepository.findAll()).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            getServices.execute()
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
