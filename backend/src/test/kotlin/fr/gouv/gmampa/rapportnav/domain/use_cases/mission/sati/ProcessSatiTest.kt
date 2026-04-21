package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.ProcessSati
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati.SatiMapper
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.sati.Sati
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import java.time.Instant
import java.util.UUID

@SpringBootTest(classes = [ProcessSati::class])
@ContextConfiguration(classes = [ProcessSati::class])
class ProcessSatiTest {

    @MockitoBean
    private lateinit var  satiRepo: ISatiRepository

    @Test
    fun `execute should return null when sati is null`() {
        val processSati = ProcessSati(satiRepo)
        val result = processSati.execute(actionId = "action-1", sati = null)
        assertThat(result).isNull()
        verifyNoInteractions(satiRepo)
    }

    @Test
    fun `execute should return entity without saving when incoming sati matches database value`() {
        val actionId = "action-1"
        val processSati = ProcessSati(satiRepo)
        val sati = createSati(actionId = actionId)
        val entity = SatiMapper.toEntity(sati)

        whenever(satiRepo.findByActionId(actionId)).thenReturn(entity)

        val result = processSati.execute(actionId = actionId, sati = sati)

        assertThat(result).isEqualTo(entity)
        verify(satiRepo).findByActionId(actionId)
    }

    @Test
    fun `execute should save and return merged entity when incoming sati differs from database value`() {
        val actionId = "action-1"
        val sati = createSati(
            id = UUID.randomUUID(),
            actionId = actionId,
            module = "AA",
            actionTaken = "Checked"
        )
        val existingInDb = createEntity(
            id = UUID.randomUUID(),
            actionId = actionId,
            module = "BB"
        )
        val processSati = ProcessSati(satiRepo)
        val entityToSave = SatiMapper.toEntity(sati)

        whenever(satiRepo.findByActionId(actionId)).thenReturn(existingInDb)
        whenever(satiRepo.save(entityToSave)).thenReturn(entityToSave)

        val result = processSati.execute(actionId = actionId, sati = sati)

        assertThat(result).isEqualTo(entityToSave)
        verify(satiRepo).findByActionId(actionId)
        verify(satiRepo).save(entityToSave)
    }

    private fun createSati(
        id: UUID? = UUID.randomUUID(),
        actionId: String,
        module: String = "MODULE",
        actionTaken: String? = null
    ): Sati {
        return Sati(
            id = id,
            module = module,
            actionId = actionId,
            createdAt = Instant.parse("2026-03-24T10:15:30Z"),
            updatedAt = Instant.parse("2026-03-24T11:15:30Z"),
            actionTaken = actionTaken,
            inspectionStartDatetimeUtc = Instant.parse("2026-03-24T09:15:30Z")
        )
    }

    private fun createEntity(
        id: UUID? = UUID.randomUUID(),
        actionId: String,
        module: String = "MODULE",
        actionTaken: String? = null
    ): SatiEntity {
        return SatiEntity(
            id = id,
            module = module,
            actionId = actionId,
            createdAt = Instant.parse("2026-03-24T10:15:30Z"),
            updatedAt = Instant.parse("2026-03-24T11:15:30Z"),
            actionTaken = actionTaken,
            inspectionStartDatetimeUtc = Instant.parse("2026-03-24T09:15:30Z")
        )
    }
}
