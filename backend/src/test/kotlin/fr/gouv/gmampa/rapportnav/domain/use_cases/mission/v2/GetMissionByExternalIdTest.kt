package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.Optional
import java.util.UUID

@SpringBootTest(classes = [GetMissionByExternalId::class])
class GetMissionByExternalIdTest {

    @Autowired
    private lateinit var useCase: GetMissionByExternalId

    @MockitoBean
    private lateinit var repository: IMissionNavRepository

    @Test
    fun `returns the local mission when one exists for the external id`() {
        val model = MissionModel(
            id = UUID.randomUUID(),
            externalId = "123",
            startDateTimeUtc = Instant.parse("2024-01-01T00:00:00Z")
        )
        whenever(repository.findByExternalId("123")).thenReturn(Optional.of(model))

        val result = useCase.execute("123")

        assertEquals(model, result)
        verify(repository).findByExternalId("123")
    }

    @Test
    fun `returns null when no local mission exists for the external id`() {
        whenever(repository.findByExternalId("999")).thenReturn(Optional.empty())

        assertNull(useCase.execute("999"))
    }
}
