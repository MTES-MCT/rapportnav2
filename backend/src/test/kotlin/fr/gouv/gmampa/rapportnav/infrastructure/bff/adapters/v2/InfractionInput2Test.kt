package fr.gouv.gmampa.rapportnav.infrastructure.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.InfractionInput2
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.InfractionTargetInput2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
class InfractionInput2Test {

    @Test
    fun `execute should convert into entity`() {
        val actionId = UUID.randomUUID().toString()
        val controlId = UUID.randomUUID().toString()

        val input = InfractionInput2(
            missionId = 761,
            controlId = controlId,
            controlType = ControlType.SECURITY.toString(),
            infractionType = InfractionTypeEnum.WITH_REPORT,
            natinfs = listOf(),
            observations = "My observations",
            target = null
        )
        input.setMissionIdAndActionId(missionId = 761, actionId = actionId)
        val entity = input.toInfractionEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.id).isNotNull()
        assertThat(entity.missionId).isEqualTo(761)
        assertThat(entity.infractionType).isEqualTo(InfractionTypeEnum.WITH_REPORT)
        assertThat(entity.controlType).isEqualTo(ControlType.SECURITY)
        assertThat(entity.actionId).isEqualTo(actionId)
        assertThat(entity.observations).isEqualTo("My observations")
        assertThat(entity.target).isEqualTo(null)
    }


    @Test
    fun `execute should convert into target entity`() {
        val actionId = UUID.randomUUID().toString()
        val controlId = UUID.randomUUID().toString()
        val target = InfractionTargetInput2(
            vesselIdentifier = "My vessel identifier",
            vesselType = VesselTypeEnum.MOTOR.toString(),
            vesselSize = VesselSizeEnum.LESS_THAN_12m.toString(),
            identityControlledPerson = "identityControlledPerson",
            vehicleType = VehicleTypeEnum.VEHICLE_AIR,
        )

        val input = InfractionInput2(
            missionId = 761,
            controlId = controlId,
            controlType = ControlType.SECURITY.toString(),
            infractionType = InfractionTypeEnum.WITH_REPORT,
            natinfs = listOf(),
            observations = "My observations",
            target = target
        )
        input.setMissionIdAndActionId(missionId = 761, actionId = actionId)
        val entity = input.toInfractionEnvTargetEntity()

        assertThat(entity).isNotNull()
        assertThat(entity.id).isNotNull()
        assertThat(entity.missionId).isEqualTo(761)
        assertThat(entity.vesselType.toString()).isEqualTo(target.vesselType)
        assertThat(entity.vesselSize.toString()).isEqualTo(target.vesselSize)
        assertThat(entity.vesselIdentifier).isEqualTo(target.vesselIdentifier)
        assertThat(entity.identityControlledPerson).isEqualTo(target.identityControlledPerson)
    }
}
