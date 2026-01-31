package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.Infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.INatinfRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetNatinfs
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetNatinfs::class])
class GetNatinfsTest {

    @MockitoBean
    private lateinit var repository: INatinfRepository

    @Test
    fun `should return all natinfs`() {
        val natinfs = listOf(
            NatinfEntity(infraction = "Infraction 1", natinfCode = 111),
            NatinfEntity(infraction = "Infraction 2", natinfCode = 222)
        )

        `when`(repository.findAll()).thenReturn(natinfs)

        val useCase = GetNatinfs(repository)
        val result = useCase.execute()

        assertThat(result).hasSize(2)
        assertThat(result[0].natinfCode).isEqualTo(111)
        assertThat(result[1].natinfCode).isEqualTo(222)
        verify(repository).findAll()
    }

    @Test
    fun `should return empty list when no natinfs`() {
        `when`(repository.findAll()).thenReturn(emptyList())

        val useCase = GetNatinfs(repository)
        val result = useCase.execute()

        assertThat(result).isEmpty()
        verify(repository).findAll()
    }

    @Test
    fun `should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        `when`(repository.findAll()).thenAnswer { throw internalException }

        val useCase = GetNatinfs(repository)

        val exception = assertThrows<BackendInternalException> {
            useCase.execute()
        }
        assertThat(exception.message).isEqualTo("API call failed")
    }
}
