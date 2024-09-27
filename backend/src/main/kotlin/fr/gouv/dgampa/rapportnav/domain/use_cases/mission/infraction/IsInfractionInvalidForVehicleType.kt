package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.infraction.InfractionWithNewTargetInput

@UseCase
class IsInfractionInvalidForVehicleType() {

    fun execute(infraction: InfractionWithNewTargetInput): String? {
        if (infraction.vehicleType == null) return null;
        when (infraction.vehicleType) {
            VehicleTypeEnum.VESSEL -> return isInValidVehicleVessel(infraction)
            else -> return isInValidOtherVehicle(infraction);
        }
    }

    private fun isInValidVehicleVessel(infraction: InfractionWithNewTargetInput): String? {
        if (infraction.vesselIdentifier == null || infraction.vesselType == null || infraction.vesselSize == null
        ) {
            return "Infraction required vessel identifier, vessel type, vessel size";
        }
        return null;
    }

    private fun isInValidOtherVehicle(infraction: InfractionWithNewTargetInput): String? {
        if (infraction.vesselIdentifier == null) {
            return "Infraction required, vessel identifier";
        }
        return null;
    }
}
