package fr.gouv.gmampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.PatchedEnvActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.PatchedEnvAction
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.*

class PatchedEnvActionTest {

    @Test
    fun `fromPatchableEnvActionEntity should map all fields`() {
        val id = UUID.randomUUID()
        val start = Instant.parse("2024-01-01T10:00:00Z")
        val end = Instant.parse("2024-01-01T12:00:00Z")
        val entity = PatchedEnvActionEntity(
            id = id,
            actionStartDateTimeUtc = start,
            actionEndDateTimeUtc = end,
            observationsByUnit = "Some observations"
        )

        val result = PatchedEnvAction.fromPatchableEnvActionEntity(entity)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(id)
        assertThat(result?.startDateTimeUtc).isEqualTo(start)
        assertThat(result?.endDateTimeUtc).isEqualTo(end)
        assertThat(result?.observationsByUnit).isEqualTo("Some observations")
    }

    @Test
    fun `fromPatchableEnvActionEntity should return null when input is null`() {
        val result = PatchedEnvAction.fromPatchableEnvActionEntity(null)
        assertThat(result).isNull()
    }

    @Test
    fun `fromPatchableEnvActionEntity should handle null optional fields`() {
        val id = UUID.randomUUID()
        val entity = PatchedEnvActionEntity(id = id)

        val result = PatchedEnvAction.fromPatchableEnvActionEntity(entity)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(id)
        assertThat(result?.startDateTimeUtc).isNull()
        assertThat(result?.endDateTimeUtc).isNull()
        assertThat(result?.observationsByUnit).isNull()
    }
}