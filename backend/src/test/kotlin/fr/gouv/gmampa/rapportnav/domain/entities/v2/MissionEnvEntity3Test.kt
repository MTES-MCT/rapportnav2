package fr.gouv.gmampa.rapportnav.domain.entities.v2


import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionEnvEntity3Test {

    @Test
    fun `execute should get env entity from nav entity`() {
        val missionNavEntity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            observationsByUnit = "myObservation",
        )
        val response = MissionEnvEntity.fromMissionNavEntity(missionNavEntity)

        assertThat(response).isNotNull()
        assertThat(response.idUUID).isEqualTo(missionNavEntity.id)
        assertThat(response.isDeleted).isEqualTo(missionNavEntity.isDeleted)
        assertThat(response.endDateTimeUtc).isEqualTo(missionNavEntity.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(missionNavEntity.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(missionNavEntity.observationsByUnit)
        assertThat(response.isUnderJdp).isEqualTo(false)
        assertThat(response.missionSource).isEqualTo(MissionSourceEnum.RAPPORT_NAV)
        assertThat(response.missionTypes?.size).isEqualTo(0)
        assertThat(response.geom).isEqualTo(null)
        assertThat(response.facade).isEqualTo(null)
    }
}
