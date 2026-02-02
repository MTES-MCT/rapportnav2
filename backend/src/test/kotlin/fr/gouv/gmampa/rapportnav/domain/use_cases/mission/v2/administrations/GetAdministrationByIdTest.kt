package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetAdministrationById::class])
class GetAdministrationByIdTest {

    @Autowired
    private lateinit var useCase: GetAdministrationById

    @MockitoBean
    private lateinit var repository: IEnvAdministrationRepository

    @Test
    fun `returns mapped administration when found`() {
        val model = FullAdministrationDataOutput(
            id = 999,
            controlUnitIds = listOf(),
            controlUnits = listOf(),
            isArchived = false,
            name = "Test Admin"
        )

        whenever(repository.findById(42)).thenReturn(model)

        val result = useCase.execute(42)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(999)
        assertThat(result?.name).isEqualTo("Test Admin")
        assertThat(result?.isArchived).isFalse()
        assertThat(result?.controlUnits).isEmpty()
        assertThat(result?.controlUnitIds).isEmpty()

        verify(repository).findById(42)
    }

    @Test
    fun `returns null when administration not found`() {
        whenever(repository.findById(99)).thenReturn(null)

        val result = useCase.execute(99)

        assertThat(result).isNull()
        verify(repository).findById(99)
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "Repository error",
            originalException = RuntimeException("API error")
        )

        whenever(repository.findById(42)).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            useCase.execute(42)
        }
        assertThat(exception.message).isEqualTo("Repository error")
    }
}
