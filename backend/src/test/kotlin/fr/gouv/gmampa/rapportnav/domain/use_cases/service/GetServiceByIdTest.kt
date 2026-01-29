package fr.gouv.gmampa.rapportnav.domain.use_cases.service

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.service.GetServiceById
import fr.gouv.gmampa.rapportnav.mocks.mission.crew.ServiceEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
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
    fun `should return service when found`() {
        val serviceModel = ServiceEntityMock.create(id = 1, name = "Test Service").toServiceModel()

        `when`(serviceRepository.findById(1)).thenReturn(Optional.of(serviceModel))

        val result = getServiceById.execute(1)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(1)
        assertThat(result?.name).isEqualTo("Test Service")
    }

    @Test
    fun `should return null when service not found`() {
        `when`(serviceRepository.findById(999)).thenReturn(Optional.empty())

        val result = getServiceById.execute(999)

        assertThat(result).isNull()
    }

    @Test
    fun `should return null when id is null`() {
        val result = getServiceById.execute(null)

        assertThat(result).isNull()
    }
}
