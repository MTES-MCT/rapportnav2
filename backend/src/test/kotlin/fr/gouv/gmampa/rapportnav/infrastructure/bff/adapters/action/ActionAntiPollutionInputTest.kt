package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionAntiPollutionInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.ZonedDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class ActionAntiPollutionInputTest {
    @Test
    fun `execute should retrieve action anti pollution entity from action Anti pol input`() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val startDateTimeUtc = ZonedDateTime.parse("2019-09-08T22:00:00.000+01:00");
        val endDateTimeUtc = ZonedDateTime.parse("2019-09-09T01:00:00.000+01:00")
        val action =  ActionAntiPollutionInput(
            missionId = 761,
            id = id,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            observations = "My beautiful observation",
            isAntiPolDeviceDeployed = true,
            isSimpleBrewingOperationDone = true,
            diversionCarriedOut = true
        )
        val navActionAntiPollution = action.toActionAntiPollutionEntity();

        assertThat(navActionAntiPollution).isNotNull();
        assertThat(navActionAntiPollution.id).isEqualTo(id);
        assertThat(navActionAntiPollution.missionId).isEqualTo(761);
        assertThat(navActionAntiPollution.startDateTimeUtc).isEqualTo(startDateTimeUtc);
        assertThat(navActionAntiPollution.endDateTimeUtc).isEqualTo(endDateTimeUtc);
        assertThat(navActionAntiPollution.isAntiPolDeviceDeployed).isEqualTo(true);
        assertThat(navActionAntiPollution.isSimpleBrewingOperationDone).isEqualTo(true);
        assertThat(navActionAntiPollution.diversionCarriedOut).isEqualTo(true);
    }
}
