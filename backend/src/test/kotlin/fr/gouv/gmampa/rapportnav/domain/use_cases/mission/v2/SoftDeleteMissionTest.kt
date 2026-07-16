package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SoftDeleteMission
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.util.UUID

@SpringBootTest(classes = [SoftDeleteMission::class])
class SoftDeleteMissionTest {

    @Autowired
    private lateinit var softDeleteMission: SoftDeleteMission

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Test
    fun `should throw exception when id is null`() {
        val exception = assertThrows<BackendUsageException> {
            softDeleteMission.execute(id = null)
        }
        assertEquals(BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION, exception.code)
        verifyNoInteractions(repository)
    }

    @Test
    fun `should delegate to repository softDeleteById when id is provided`() {
        val id = UUID.randomUUID()

        softDeleteMission.execute(id = id)

        verify(repository).softDeleteById(id)
    }
}
