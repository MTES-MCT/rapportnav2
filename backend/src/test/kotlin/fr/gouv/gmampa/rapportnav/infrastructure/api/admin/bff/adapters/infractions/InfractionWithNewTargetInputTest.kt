package fr.gouv.gmampa.rapportnav.infrastructure.api.admin.bff.adapters.infractions

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.infraction.InfractionWithNewTargetInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
class InfractionWithNewTargetInputTest {

    @Test
    fun `execute should retrieve infraction entity from infraction with new target input`() {
        val actionId = "MyActionID";
        val controlType = ControlType.SECURITY.toString()
        val identityControlledPerson = "identityControlledPerson";
        val action = InfractionWithNewTargetInput(
            missionId = 761,
            actionId = actionId,
            controlType = controlType,
            identityControlledPerson = identityControlledPerson,
            vesselIdentifier = "myVesselIdentifier",
            vesselSize = VesselSizeEnum.FROM_24_TO_46m.toString(),
            vesselType = VesselTypeEnum.FISHING.toString()
        )
        val infractionEntity = action.toInfractionEntity();

        assertThat(infractionEntity).isNotNull();
        assertThat(infractionEntity.missionId).isEqualTo(761);
        assertThat(infractionEntity.controlType).isEqualTo(ControlType.SECURITY);
        assertThat(infractionEntity.target?.vesselSize).isEqualTo(VesselSizeEnum.FROM_24_TO_46m);
        assertThat(infractionEntity.target?.vesselType).isEqualTo(VesselTypeEnum.FISHING);
        assertThat(infractionEntity.target?.vesselIdentifier).isEqualTo("myVesselIdentifier");
        assertThat(infractionEntity.target?.identityControlledPerson).isEqualTo(identityControlledPerson);

    }

    @Test
    fun `execute should retrieve infraction entity event if vessel is null `() {
        val id = UUID.fromString("0000-00-00-00-000000");
        val actionId = "MyActionID";
        val controlType = ControlType.SECURITY.toString()
        val identityControlledPerson = "identityControlledPerson";
        val action = InfractionWithNewTargetInput(
            missionId = 761,
            actionId = actionId,
            controlType = controlType,
            identityControlledPerson = identityControlledPerson,
        )
        val infractionEntity = action.toInfractionEntity();
        assertThat(infractionEntity).isNotNull();
        assertThat(infractionEntity.target?.vesselSize).isNull();
        assertThat(infractionEntity.target?.vesselType).isNull();
        assertThat(infractionEntity.target?.vesselIdentifier).isNull();

    }
}
