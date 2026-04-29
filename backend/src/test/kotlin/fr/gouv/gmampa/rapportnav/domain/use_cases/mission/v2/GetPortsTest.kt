package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetPorts
import fr.gouv.gmampa.rapportnav.mocks.mission.PortEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetPorts::class])
class GetPortsTest {
    @Autowired
    private lateinit var useCase: GetPorts

    @MockitoBean
    private lateinit var repository: IFishActionRepository

    @Test
    fun `execute should return list of ports from repository`() {
        val port = PortEntityMock.create()
        whenever(repository.getPorts()).thenReturn(listOf(port))

        val result = useCase.execute()

        assertEquals(1, result.size)
        assertEquals(port.locode, result[0].locode)
        assertEquals(port.name, result[0].name)
        verify(repository).getPorts()
    }

    @Test
    fun `execute should return empty list when no ports`() {
        whenever(repository.getPorts()).thenReturn(emptyList())

        val result = useCase.execute()

        assertThat(result).isEmpty()
        verify(repository).getPorts()
    }

    @Test
    fun `execute with name should filter by name prefix`() {
        val ports = listOf(
            PortEntityMock.create(locode = "FRLEH", name = "Le Havre"),
            PortEntityMock.create(locode = "FRMRS", name = "Marseille")
        )
        whenever(repository.getPorts()).thenReturn(ports)

        val result = useCase.execute(name = "Le")

        assertThat(result).hasSize(1)
        assertThat(result[0].locode).isEqualTo("FRLEH")
    }

    @Test
    fun `execute with name should match by exact locode`() {
        val ports = listOf(
            PortEntityMock.create(locode = "FRLEH", name = "Le Havre"),
            PortEntityMock.create(locode = "FRMRS", name = "Marseille")
        )
        whenever(repository.getPorts()).thenReturn(ports)

        val result = useCase.execute(name = "FRMRS")

        assertThat(result).hasSize(1)
        assertThat(result[0].locode).isEqualTo("FRMRS")
        assertThat(result[0].name).isEqualTo("Marseille")
    }

    @Test
    fun `execute with blank name should return empty list`() {
        val result = useCase.execute(name = "")
        assertThat(result).isEmpty()
    }

    @Test
    fun `execute should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        whenever(repository.getPorts()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            useCase.execute()
        }
        assertThat(exception.message).isEqualTo("API call failed")
    }
}
