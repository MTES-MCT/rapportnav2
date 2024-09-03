package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.PatchedEnvAction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

class PatchedEnvActionTest {

    @Test
    fun `fromPatchableEnvActionEntity should correctly map properties`() {
        val id = UUID.randomUUID()
        val startTime = Instant.now()
        val endTime = startTime.plus(1, ChronoUnit.HOURS)
        val observations = "Some observations"

        val entity = PatchedEnvActionEntity(
            id = id,
            actionStartDateTimeUtc = startTime,
            actionEndDateTimeUtc = endTime,
            observationsByUnit = observations
        )

        val result = PatchedEnvAction.fromPatchableEnvActionEntity(entity)

        assertNotNull(result)
        assertEquals(id, result?.id)
        assertEquals(startTime, result?.startDateTimeUtc)
        assertEquals(endTime, result?.endDateTimeUtc)
        assertEquals(observations, result?.observationsByUnit)
    }

    @Test
    fun `fromPatchableEnvActionEntity should return null for null input`() {
        val result = PatchedEnvAction.fromPatchableEnvActionEntity(null)
        assertNull(result)
    }

    @Test
    fun `fromPatchableEnvActionEntity should handle null properties`() {
        val id = UUID.randomUUID()
        val entity = PatchedEnvActionEntity(
            id = id,
            actionStartDateTimeUtc = null,
            actionEndDateTimeUtc = null,
            observationsByUnit = null
        )

        val result = PatchedEnvAction.fromPatchableEnvActionEntity(entity)

        assertNotNull(result)
        assertEquals(id, result?.id)
        assertNull(result?.startDateTimeUtc)
        assertNull(result?.endDateTimeUtc)
        assertNull(result?.observationsByUnit)
    }

}
