package fr.gouv.gmampa.rapportnav.domain.entities.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavInputEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.wiremock.utils.LoadJsonData
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class MissionNavEntityTest {


    @Test
    fun `execute should get model from entity`() {
        val model = MissionModel(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            openBy = "by Cacem",
            observationsByUnit = "myObservation",
        )
        val response = MissionNavEntity.fromMissionModel(model)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(model.id)
        assertThat(response.openBy).isEqualTo(model.openBy)
        assertThat(response.isDeleted).isEqualTo(model.isDeleted)
        assertThat(response.endDateTimeUtc).isEqualTo(model.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(model.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(model.observationsByUnit)
    }

    @Test
    fun `execute should get entity from model`() {
        val entity = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            openBy = "by Cacem",
            observationsByUnit = "myObservation",
        )
        val response = entity.toMissionModel()

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(entity.id)
        assertThat(response.openBy).isEqualTo(entity.openBy)
        assertThat(response.isDeleted).isEqualTo(entity.isDeleted)
        assertThat(response.endDateTimeUtc).isEqualTo(entity.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(entity.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(entity.observationsByUnit)
    }


    @Test
    fun `execute should detect if mission has changed`() {
        val id = UUID.randomUUID()
        val startDateTimeUtc = Instant.now()
        val endDateTimeUtc = Instant.now()
        val observationsByUnit = "myObservation"
        val mission = MissionNavEntity(
            id = id,
            serviceId = 2,
            endDateTimeUtc = startDateTimeUtc,
            startDateTimeUtc = endDateTimeUtc,
            isDeleted = false,
            openBy = "by Cacem",
            observationsByUnit = observationsByUnit,
        )

        val input = MissionNavInputEntity(
            completedBy = "",
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isDeleted = false,
            observationsByUnit = observationsByUnit
        )

        val response = mission.hasNotChanged(input = input)

        assertThat(response).isEqualTo(false)
    }



    @Test
    fun `execute should merge input into entity to get model`() {
        val mission = MissionNavEntity(
            id = UUID.randomUUID(),
            serviceId = 2,
            endDateTimeUtc = Instant.now(),
            startDateTimeUtc = Instant.now(),
            isDeleted = false,
            openBy = "by Cacem",
            observationsByUnit = "myObservation",
        )

        val input = MissionNavInputEntity(
            completedBy = "TOTO",
            startDateTimeUtc =  Instant.parse("2019-09-09T00:00:00.000+01:00"),
            endDateTimeUtc =  Instant.parse("2019-09-09T00:00:00.000+01:00"),
            isDeleted = true,
            observationsByUnit = "myNew observation"
        )

        val response = mission.fromMissionNavInput(input = input)

        assertThat(response).isNotNull()
        assertThat(response.id).isEqualTo(mission.id)
        assertThat(response.openBy).isEqualTo(mission.openBy)
        assertThat(response.isDeleted).isEqualTo(input.isDeleted)
        assertThat(response.endDateTimeUtc).isEqualTo(input.endDateTimeUtc)
        assertThat(response.startDateTimeUtc).isEqualTo(input.startDateTimeUtc)
        assertThat(response.observationsByUnit).isEqualTo(input.observationsByUnit)
    }

/*
    @Test
    fun `execute test of completions`() {
        val mission = LoadJsonData.loadToMission("nav/mission.json")
        val entity = mission?.toMissionEntity()
        assertThat(entity).isNotNull
        val isCompleteForStats = entity?.isCompleteForStats()
        assertThat(isCompleteForStats).isEqualTo(true)
    }*/
}
