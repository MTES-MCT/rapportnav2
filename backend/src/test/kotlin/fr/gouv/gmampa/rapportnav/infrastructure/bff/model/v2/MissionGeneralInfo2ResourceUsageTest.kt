package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.ControlUnitResourceType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.LegacyControlUnitResource
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class MissionGeneralInfo2ResourceUsageTest {

    private val missionId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")

    private val car = LegacyControlUnitResource(
        id = 338, name = "Voiture", controlUnitId = 10176, type = ControlUnitResourceType.CAR, nbKms = 42.0
    )
    private val boat = LegacyControlUnitResource(
        id = 1252, name = "PM 445", controlUnitId = 10176, type = ControlUnitResourceType.SEMI_RIGID, nbEngineHours = 3.5
    )

    private fun pamGeneralInfo(resources: List<LegacyControlUnitResource> = listOf()) = MissionGeneralInfo2(
        resources = resources,
        distanceInNauticalMiles = 12.3f,
        consumedGOInLiters = 3400f,
        consumedFuelInLiters = 20f,
        operatingCostsInEuro = 900.5f,
        fuelCostsInEuro = 4100f
    )

    @Test
    fun `ULAM resources map to per-resource rows, ignoring values that do not match the type`() {
        val carWithEngineHours = car.copy(nbEngineHours = 9.0)
        val generalInfo = MissionGeneralInfo2(resources = listOf(carWithEngineHours, boat))

        val rows = generalInfo.toResourceUsageEntities(missionId)

        assertThat(rows).hasSize(2)
        val carRow = rows.first { it.resourceId == 338 }
        assertThat(carRow.nbKms).isEqualTo(42.0)
        assertThat(carRow.nbEngineHours).isNull()
        assertThat(rows.first { it.resourceId == 1252 }.nbEngineHours).isEqualTo(3.5)
        assertThat(rows.map { it.missionIdUUID }.distinct()).containsExactly(missionId)
    }

    @Test
    fun `PAM metrics are ignored without a ship resource id`() {
        val rows = pamGeneralInfo().toResourceUsageEntities(missionId)

        assertThat(rows).isEmpty()
    }

    @Test
    fun `PAM metrics produce one row on the ship with exact decimal values`() {
        val rows = pamGeneralInfo().toResourceUsageEntities(missionId, pamShipResourceId = 582)

        val row = rows.single()
        assertThat(row.missionIdUUID).isEqualTo(missionId)
        assertThat(row.resourceId).isEqualTo(582)
        assertThat(row.distanceInNauticalMiles).isEqualTo(12.3)
        assertThat(row.consumedGOInLiters).isEqualTo(3400.0)
        assertThat(row.consumedFuelInLiters).isEqualTo(20.0)
        assertThat(row.nbKms).isNull()
        assertThat(row.nbEngineHours).isNull()
    }

    @Test
    fun `no PAM row when all five PAM values are null`() {
        val rows = MissionGeneralInfo2(resources = listOf(car)).toResourceUsageEntities(missionId, pamShipResourceId = 582)

        assertThat(rows.map { it.resourceId }).containsExactly(338)
    }

    @Test
    fun `PAM row is added alongside the per-resource rows`() {
        val rows = pamGeneralInfo(resources = listOf(car)).toResourceUsageEntities(missionId, pamShipResourceId = 582)

        assertThat(rows.map { it.resourceId }).containsExactlyInAnyOrder(338, 582)
        assertThat(rows.first { it.resourceId == 338 }.distanceInNauticalMiles).isNull()
        assertThat(rows.first { it.resourceId == 582 }.distanceInNauticalMiles).isEqualTo(12.3)
    }

    @Test
    fun `PAM metrics merge into the ship's per-resource row instead of duplicating it`() {
        val ship = LegacyControlUnitResource(
            id = 582, name = "Iris", controlUnitId = 10404, type = ControlUnitResourceType.PATROL_BOAT, nbEngineHours = 7.0
        )

        val rows = pamGeneralInfo(resources = listOf(ship)).toResourceUsageEntities(missionId, pamShipResourceId = 582)

        val row = rows.single()
        assertThat(row.resourceId).isEqualTo(582)
        assertThat(row.nbEngineHours).isEqualTo(7.0)
        assertThat(row.distanceInNauticalMiles).isEqualTo(12.3)
        assertThat(row.consumedFuelInLiters).isEqualTo(20.0)
    }

    @Test
    fun `mission costs alone do not produce a ship row`() {
        // The costs belong to the mission (mission_general_info), not to a resource.
        val costsOnly = MissionGeneralInfo2(operatingCostsInEuro = 900.5f, fuelCostsInEuro = 4100f)

        assertThat(costsOnly.toResourceUsageEntities(missionId, pamShipResourceId = 582)).isEmpty()
    }
}
