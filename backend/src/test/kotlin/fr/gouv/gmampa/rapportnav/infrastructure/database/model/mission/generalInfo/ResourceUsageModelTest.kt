package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.ResourceUsageModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.UUID

class ResourceUsageModelTest {

    private val missionId: UUID = UUID.fromString("11111111-1111-1111-1111-111111111111")

    @Test
    fun `fromResourceUsageEntity then toResourceUsageEntity round-trips every field`() {
        val entity = ResourceUsageEntity(
            id = 1,
            missionIdUUID = missionId,
            resourceId = 582,
            nbKms = 12.5,
            nbEngineHours = 4.0,
            distanceInNauticalMiles = 120.5,
            consumedGOInLiters = 3400.0,
            consumedFuelInLiters = 20.0
        )

        val model = ResourceUsageModel.fromResourceUsageEntity(entity)

        assertThat(model.id).isEqualTo(1)
        assertThat(model.missionIdUUID).isEqualTo(missionId)
        assertThat(model.resourceId).isEqualTo(582)
        assertThat(model.distanceInNauticalMiles).isEqualTo(120.5)
        assertThat(model.toResourceUsageEntity()).isEqualTo(entity)
    }

    @Test
    fun `PAM metrics default to null`() {
        val model = ResourceUsageModel(missionIdUUID = missionId, resourceId = 7, nbKms = 10.0)

        val entity = model.toResourceUsageEntity()

        assertThat(entity.nbKms).isEqualTo(10.0)
        assertThat(entity.distanceInNauticalMiles).isNull()
        assertThat(entity.consumedGOInLiters).isNull()
        assertThat(entity.consumedFuelInLiters).isNull()
    }
}
