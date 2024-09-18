package fr.gouv.gmampa.rapportnav.domain.entities.mission.nav.action;

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionAntiPollutionEntity
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.Instant
import java.util.*

@ExtendWith(SpringExtension::class)
class ActionAntiPollutionEntityTest {
    @Test
    fun `execute should retrieve nav action anti pollution from entity`() {
        val id = UUID.fromString("0000-00-00-00-000000");
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
        val navActionAntiPollution = action.toNavActionAntiPollution();

        assertThat(navActionAntiPollution).isNotNull();
        assertThat(navActionAntiPollution.id).isEqualTo(id);
        assertThat(navActionAntiPollution.missionId).isEqualTo(761);
        assertThat(navActionAntiPollution.startDateTimeUtc).isEqualTo(startDateTimeUtc);
        assertThat(navActionAntiPollution.endDateTimeUtc).isEqualTo(endDateTimeUtc);
        assertThat(navActionAntiPollution.isAntiPolDeviceDeployed).isEqualTo(true);
        assertThat(navActionAntiPollution.isSimpleBrewingOperationDone).isEqualTo(true);
        assertThat(navActionAntiPollution.diversionCarriedOut).isEqualTo(true);
    }

    @Test
    fun `execute should retrieve nav action entity anti pollution from entity`() {
        val id = UUID.fromString("0000-00-00-00-000000");
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
        val navActionEntity = action.toNavActionEntity()

        assertThat(navActionEntity).isNotNull();
        assertThat(navActionEntity.id).isEqualTo(id);
        assertThat(navActionEntity.missionId).isEqualTo(761);
        assertThat(navActionEntity.startDateTimeUtc).isEqualTo(startDateTimeUtc);
        assertThat(navActionEntity.endDateTimeUtc).isEqualTo(endDateTimeUtc);
        assertThat(navActionEntity.antiPollutionAction?.isAntiPolDeviceDeployed).isEqualTo(true);
        assertThat(navActionEntity.antiPollutionAction?.isSimpleBrewingOperationDone).isEqualTo(true);
        assertThat(navActionEntity.antiPollutionAction?.diversionCarriedOut).isEqualTo(true);
    }
}
