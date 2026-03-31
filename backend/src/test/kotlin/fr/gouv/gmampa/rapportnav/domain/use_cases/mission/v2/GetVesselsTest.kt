package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels
import fr.gouv.gmampa.rapportnav.mocks.mission.VesselEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(classes = [GetVessels::class])
class GetVesselsTest {
    @Autowired
    private lateinit var useCase: GetVessels

    @MockitoBean
    private lateinit var repository: IFishActionRepository

    @Test
    fun `execute should return list of vessels from repository`() {
        val vessel = VesselEntityMock.create()
        whenever(repository.getVessels()).thenReturn(listOf(vessel))

        val result = useCase.execute()

        assertEquals(1, result.size)
        assertEquals(vessel.vesselId, result[0].vesselId)
        assertEquals(vessel.vesselName, result[0].vesselName)
        verify(repository).getVessels()
    }

    @Test
    fun `execute should return empty list when no vessels`() {
        whenever(repository.getVessels()).thenReturn(emptyList())

        val result = useCase.execute()

        assertThat(result).isEmpty()
        verify(repository).getVessels()
    }

    @Test
    fun `execute should propagate BackendInternalException from repository`() {
        val internalException = BackendInternalException(
            message = "API call failed",
            originalException = RuntimeException("Connection error")
        )

        whenever(repository.getVessels()).thenAnswer { throw internalException }

        val exception = assertThrows<BackendInternalException> {
            useCase.execute()
        }
        assertThat(exception.message).isEqualTo("API call failed")
    }
}
