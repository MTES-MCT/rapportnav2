package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType


data class InfractionsByVessel(
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val controlTypesWithInfraction: List<ControlType>? = null,
    val targetAddedByUnit: Boolean? = null,
    val infractions: List<Infraction>
) {
    fun groupInfractionsByVesselIdentifier(infractions: List<Infraction>): List<InfractionsByVessel> {
        return infractions
            .groupBy { it.target?.vesselIdentifier }
            .map { (vesselIdentifier, infractions) ->
                InfractionsByVessel(
                    vesselIdentifier = vesselIdentifier,
                    vesselType = infractions.firstOrNull()?.target?.vesselType,
                    infractions = infractions,
                    controlTypesWithInfraction = controlTypesWithInfraction,
                    targetAddedByUnit = targetAddedByUnit,
                )
            }
    }
}
