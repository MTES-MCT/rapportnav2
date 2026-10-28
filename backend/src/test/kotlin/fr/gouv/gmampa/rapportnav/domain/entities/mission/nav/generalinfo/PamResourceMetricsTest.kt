package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.generalinfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamMetricsMergeResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.gmampa.rapportnav.mocks.mission.MissionGeneralInfoEntityMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class PamResourceMetricsTest {

    @Test
    fun `fromFloats keeps the decimal value instead of widening the binary float`() {
        // Sanity check of the problem being avoided.
        assertThat(12.3f.toDouble()).isNotEqualTo(12.3)

        val metrics = PamResourceMetrics.fromFloats(12.3f, 1234567.8f, null)

        assertThat(metrics.distanceInNauticalMiles).isEqualTo(12.3)
        assertThat(metrics.consumedGOInLiters).isEqualTo(1234567.8)
        assertThat(metrics.consumedFuelInLiters).isNull()
    }

    @Test
    fun `fromMissionGeneralInfoEntity takes the usage values and leaves the mission costs out`() {
        val generalInfo = MissionGeneralInfoEntityMock.create(
            distanceInNauticalMiles = 12.3f,
            consumedGOInLiters = 3400.5f,
            consumedFuelInLiters = 0.1f,
            operatingCostsInEuro = 900f,
            fuelCostsInEuro = 4100f
        )

        assertThat(PamResourceMetrics.fromMissionGeneralInfoEntity(generalInfo))
            .isEqualTo(PamResourceMetrics(distanceInNauticalMiles = 12.3, consumedGOInLiters = 3400.5, consumedFuelInLiters = 0.1))
    }

    @Test
    fun `hasAny is false only when all three values are null`() {
        assertThat(PamResourceMetrics().hasAny()).isFalse()
        assertThat(PamResourceMetrics.fromFloats(null, null, null).hasAny()).isFalse()
        assertThat(PamResourceMetrics(consumedFuelInLiters = 0.0).hasAny()).isTrue()
    }

    @Test
    fun `mergeOutcome compares only the PAM columns`() {
        val missionId = UUID.randomUUID()
        val metrics = PamResourceMetrics(distanceInNauticalMiles = 12.3)
        val sameValues = ResourceUsageEntity(missionIdUUID = missionId, resourceId = 582, nbEngineHours = 4.0, distanceInNauticalMiles = 12.3)
        val otherValues = sameValues.copy(distanceInNauticalMiles = 10.0)

        assertThat(metrics.mergeOutcome(null)).isEqualTo(PamMetricsMergeResult.CREATED)
        assertThat(PamResourceMetrics().mergeOutcome(null)).isEqualTo(PamMetricsMergeResult.UNCHANGED)
        assertThat(metrics.mergeOutcome(sameValues)).isEqualTo(PamMetricsMergeResult.UNCHANGED)
        assertThat(metrics.mergeOutcome(otherValues)).isEqualTo(PamMetricsMergeResult.UPDATED)
    }

    @Test
    fun `fromResourceUsageEntity reads only the PAM columns`() {
        val entity = ResourceUsageEntity(
            missionIdUUID = UUID.randomUUID(),
            resourceId = 582,
            nbEngineHours = 4.0,
            distanceInNauticalMiles = 120.0,
            consumedGOInLiters = 3400.0,
            consumedFuelInLiters = 20.0
        )

        assertThat(PamResourceMetrics.fromResourceUsageEntity(entity))
            .isEqualTo(PamResourceMetrics(120.0, 3400.0, 20.0))
    }
}
