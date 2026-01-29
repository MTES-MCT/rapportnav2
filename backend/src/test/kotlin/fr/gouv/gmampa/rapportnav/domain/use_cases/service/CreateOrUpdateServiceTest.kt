package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.CreateOrUpdateService
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [CreateOrUpdateService::class])
@ContextConfiguration(classes = [CreateOrUpdateService::class])
class CreateOrUpdateServiceTest {

    @Autowired
    private lateinit var createOrUpdateService: CreateOrUpdateService

    @MockitoBean
    private lateinit var serviceRepository: IServiceRepository

    @Test
    fun `should create a new service`() {
        val serviceEntity = ServiceEntityMock.create(name = "New Service")
        val savedModel = ServiceEntityMock.create(id = 1, name = "New Service").toServiceModel()

        `when`(serviceRepository.save(any())).thenReturn(savedModel)

        val result = createOrUpdateService.execute(serviceEntity)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("New Service")
    }

    @Test
    fun `should update an existing service`() {
        val serviceEntity = ServiceEntityMock.create(id = 1, name = "Updated Service")
        val savedModel = serviceEntity.toServiceModel()

        `when`(serviceRepository.save(any())).thenReturn(savedModel)

        val result = createOrUpdateService.execute(serviceEntity)

        assertThat(result).isNotNull
        assertThat(result.id).isEqualTo(1)
        assertThat(result.name).isEqualTo("Updated Service")
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val serviceEntity = ServiceEntityMock.create(name = "Test Service")
        val exception = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("Cause")
        )

        `when`(serviceRepository.save(any())).thenAnswer { throw exception }

        val thrown = assertThrows<BackendInternalException> {
            createOrUpdateService.execute(serviceEntity)
        }
        assertThat(thrown.message).isEqualTo("Repository error")
    }
}
