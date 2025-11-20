package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource

import fr.gouv.dgampa.rapportnav.domain.repositories.v2.controlUnitResource.IEnvControlUnitResourceRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetControlUnitResources
import fr.gouv.gmampa.rapportnav.mocks.mission.env.ControlUnitResourceEnvMock
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetControlUnitResources::class])
class GetControlUnitResourcesTest {

    @Autowired
    private lateinit var useCase: GetControlUnitResources

    @MockitoBean
    private lateinit var repository: IEnvControlUnitResourceRepository

    @Test
    fun `returns all control unit resources`() {
        val resource1 = ControlUnitResourceEnvMock.create(id = 1, name = "R1")
        val resource2 = ControlUnitResourceEnvMock.create(id = 2, name = "R2")

        whenever(repository.findAll()).thenReturn(listOf(resource1, resource2))

        val result = useCase.execute()

        assertEquals(2, result?.size)
        assertEquals(resource1, result?.get(0))
        assertEquals(resource2, result?.get(1))

        verify(repository).findAll()
    }

    @Test
    fun `returns empty list when repository returns empty`() {
        whenever(repository.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertTrue(result?.isEmpty() == true)
        verify(repository).findAll()
    }
}
