package fr.gouv.gmampa.rapportnav.infrastructure.database.model.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ActionAntiPollutionModel::class])
class ActionAntiPollutionModelTest {

    @Test
    fun `execute should retrieve action anti pollution entity from model`() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00");
        val endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        val action = ActionAntiPollutionModel(
            missionId = 761,
            id = id,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true
        )
        val actionAntiPollution = action.toAntiPollutionEntity();

        assertThat(actionAntiPollution).isNotNull();
        assertThat(actionAntiPollution.id).isEqualTo(id);
        assertThat(actionAntiPollution.missionId).isEqualTo(761);
        assertThat(actionAntiPollution.startDateTimeUtc).isEqualTo(startDateTimeUtc);
        assertThat(actionAntiPollution.endDateTimeUtc).isEqualTo(endDateTimeUtc);
        assertThat(actionAntiPollution.isAntiPolDeviceDeployed).isEqualTo(true);
        assertThat(actionAntiPollution.isSimpleBrewingOperationDone).isEqualTo(true);
        assertThat(actionAntiPollution.diversionCarriedOut).isEqualTo(true);
    }

    @Test
    fun `execute should retrieve action anti pollution model from entity`() {
        val id = UUID.randomUUID();
        val startDateTimeUtc = Instant.parse("2019-09-08T22:00:00.000+01:00");
        val endDateTimeUtc = Instant.parse("2019-09-09T01:00:00.000+01:00")
        val action = ActionAntiPollutionEntity(
            missionId = 761,
            id = id,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true
        )
        val actionAntiPollutionEntity = ActionAntiPollutionModel.fromAntiPollutionEntity(action);
        assertThat(actionAntiPollutionEntity).isNotNull();
        assertThat(actionAntiPollutionEntity.id).isEqualTo(id);
        assertThat(actionAntiPollutionEntity.missionId).isEqualTo(761);
        assertThat(actionAntiPollutionEntity.startDateTimeUtc).isEqualTo(startDateTimeUtc);
        assertThat(actionAntiPollutionEntity.endDateTimeUtc).isEqualTo(endDateTimeUtc);
        assertThat(actionAntiPollutionEntity.isAntiPolDeviceDeployed).isEqualTo(true);
        assertThat(actionAntiPollutionEntity.isSimpleBrewingOperationDone).isEqualTo(true);
        assertThat(actionAntiPollutionEntity.diversionCarriedOut).isEqualTo(true);
    }
}
