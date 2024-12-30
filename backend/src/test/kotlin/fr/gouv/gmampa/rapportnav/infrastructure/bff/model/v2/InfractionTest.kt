package fr.gouv.gmampa.rapportnav.infrastructure.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.InfractionTarget
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*


@ExtendWith(SpringExtension::class)
class InfractionTest {

    @Test
    fun `execute should convert into entity`() {
        val actionId = UUID.randomUUID().toString()
        val controlId = UUID.randomUUID().toString()

        val input = Infraction(
            missionId = 761,
            controlId = controlId,
            controlType = ControlType.SECURITY,
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
        val target = InfractionTarget(
            vesselIdentifier = "My vessel identifier",
            vesselType = VesselTypeEnum.MOTOR,
            vesselSize = VesselSizeEnum.LESS_THAN_12m,
            identityControlledPerson = "identityControlledPerson",
            vehicleType = VehicleTypeEnum.VEHICLE_AIR,
        )

        val input = Infraction(
            missionId = 761,
            controlId = controlId,
            controlType = ControlType.SECURITY,
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
        assertThat(entity.vesselType).isEqualTo(target.vesselType)
        assertThat(entity.vesselSize).isEqualTo(target.vesselSize)
        assertThat(entity.vesselIdentifier).isEqualTo(target.vesselIdentifier)
        assertThat(entity.identityControlledPerson).isEqualTo(target.identityControlledPerson)
    }
}
