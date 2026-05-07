package fr.gouv.gmampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.PatchedFishAction
import fr.gouv.gmampa.rapportnav.mocks.mission.action.FishActionControlMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant

class PatchedFishActionTest {

    @Test
    fun `fromMissionAction should map all fields`() {
        val start = Instant.parse("2024-01-01T10:00:00Z")
        val end = Instant.parse("2024-01-01T12:00:00Z")
        val action = FishActionControlMock.create(
            id = 42,
            actionDatetimeUtc = start,
            actionEndDatetimeUtc = end
        ).apply { observationsByUnit = "Fish observations" }

        val result = PatchedFishAction.fromMissionAction(action)

        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(42)
        assertThat(result?.startDateTimeUtc).isEqualTo(start)
        assertThat(result?.endDateTimeUtc).isEqualTo(end)
        assertThat(result?.observationsByUnit).isEqualTo("Fish observations")
    }

    @Test
    fun `fromMissionAction should return null when input is null`() {
        val result = PatchedFishAction.fromMissionAction(null)
        assertThat(result).isNull()
    }

    @Test
    fun `fromMissionAction should handle null optional fields`() {
        val action = FishActionControlMock.create(actionEndDatetimeUtc = null)

        val result = PatchedFishAction.fromMissionAction(action)

        assertThat(result).isNotNull
        assertThat(result?.endDateTimeUtc).isNull()
        assertThat(result?.observationsByUnit).isNull()
    }
}