package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2.administrations

import fr.gouv.dgampa.rapportnav.domain.repositories.v2.IEnvAdministrationRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.administrations.GetAdministrationById
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs.FullAdministrationDataOutput
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
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

        // Verify the mapping works
        assertEquals(999, result?.id)
        assertEquals("Test Admin", result?.name)
        assertEquals(false, result?.isArchived)
        assertEquals(0, result?.controlUnits?.size)
        assertEquals(0, result?.controlUnitIds?.size)

        verify(repository).findById(42)
    }

    @Test
    fun `returns null when administration not found`() {
        whenever(repository.findById(99)).thenReturn(null)

        val result = useCase.execute(99)

        assertEquals(null, result)
        verify(repository).findById(99)
    }
}
