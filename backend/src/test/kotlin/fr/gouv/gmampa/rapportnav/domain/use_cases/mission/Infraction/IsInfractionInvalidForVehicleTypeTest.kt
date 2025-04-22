package fr.gouv.gmampa.rapportnav.domain.use_cases.mission.Infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.IsInfractionInvalidForVehicleType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.infraction.InfractionWithNewTargetInput
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest(classes = [IsInfractionInvalidForVehicleType::class])
class IsInfractionInvalidForVehicleTypeTest {

    @Autowired
    private lateinit var isInfractionInvalidForVehicle: IsInfractionInvalidForVehicleType

    @Test
    fun `should return null for an input with vehicleType null`() {
        val input = InfractionWithNewTargetInput(
            missionId = "761",
            actionId = "actionId",
            controlType = "controlType",
            identityControlledPerson = "identityControlledPerson"
        )
        val response = isInfractionInvalidForVehicle.execute(input);
        assertThat(response).isNull();
    }

    @Test
    fun `should return error if vehicle is not a vessel but hasn't a vehicle identifier`() {
        val input = InfractionWithNewTargetInput(
            missionId = "761",
            actionId = "actionId",
            controlType = "controlType",
            identityControlledPerson = "identityControlledPerson",
            vehicleType = VehicleTypeEnum.VEHICLE_LAND
        )
        val response = isInfractionInvalidForVehicle.execute(input);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo("Infraction required, vessel identifier")
    }

    @Test
    fun `should return error even if the vehicle is vessel and has a vehicle identifier`() {
        val input = InfractionWithNewTargetInput(
            missionId = "761",
            actionId = "actionId",
            controlType = "controlType",
            identityControlledPerson = "identityControlledPerson",
            vehicleType = VehicleTypeEnum.VESSEL,
            vesselIdentifier = "myVesselIdentifier",
        )
        val response = isInfractionInvalidForVehicle.execute(input);
        assertThat(response).isNotNull();
        assertThat(response).isEqualTo("Infraction required vessel identifier, vessel type, vessel size")
    }

    @Test
    fun `should not return error if the vehicle is vessel and has identifier, a type, and a size`() {
        val input = InfractionWithNewTargetInput(
            missionId = "761",
            actionId = "actionId",
            controlType = "controlType",
            identityControlledPerson = "identityControlledPerson",
            vehicleType = VehicleTypeEnum.VESSEL,
            vesselIdentifier = "myVesselIdentifier",
            vesselType = VesselTypeEnum.FISHING.toString(),
            vesselSize = VesselSizeEnum.FROM_24_TO_46m.toString()
        )
        val response = isInfractionInvalidForVehicle.execute(input);
        assertThat(response).isNull();
    }
}
