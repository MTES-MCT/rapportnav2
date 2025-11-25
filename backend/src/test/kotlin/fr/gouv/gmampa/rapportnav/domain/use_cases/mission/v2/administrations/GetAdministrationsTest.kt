package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrations
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
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

        assertEquals(2, result?.size)
        assertEquals(1, result?.get(0)?.id)
        assertEquals("Admin1", result?.get(0)?.name)
        assertEquals(false, result?.get(0)?.isArchived)

        assertEquals(2, result?.get(1)?.id)
        assertEquals("Admin2", result?.get(1)?.name)
        assertEquals(true, result?.get(1)?.isArchived)

        verify(repository).findAll()
    }

    @Test
    fun `returns empty list when repository returns empty`() {
        whenever(repository.findAll()).thenReturn(emptyList())

        val result = useCase.execute()

        assertTrue(result?.isEmpty() ?: false)
        verify(repository).findAll()
    }
}
