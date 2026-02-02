package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAdministrations::class])
class GetAdministrationsTest {

    @Autowired
    private lateinit var useCase: GetAdministrations

    @MockitoBean
    private lateinit var repository: IEnvAdministrationRepository

    @Test
    fun `returns mapped list when repository has administrations`() {
        val admin1 = FullAdministrationDataOutput(
            id = 1,
            controlUnitIds = listOf(10),
            controlUnits = listOf(),
            isArchived = false,
            name = "Admin1"
        )
        val admin2 = FullAdministrationDataOutput(
            id = 2,
            controlUnitIds = listOf(20),
            controlUnits = listOf(),
            isArchived = true,
            name = "Admin2"
        )

        whenever(repository.findAll()).thenReturn(listOf(admin1, admin2))

        val result = useCase.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(1)
        assertThat(result[0].name).isEqualTo("Admin1")
        assertThat(result[0].isArchived).isFalse()

        assertThat(result[1].id).isEqualTo(2)
        assertThat(result[1].name).isEqualTo("Admin2")
        assertThat(result[1].isArchived).isTrue()

        verify(repository).findAll()
    }

    @Test
    fun `returns empty list when repository returns empty`() {
        whenever(repository.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertThat(result).isEmpty()
        verify(repository).findAll()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("API error")
        )

        whenever(repository.findAll()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            useCase.execute()
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
