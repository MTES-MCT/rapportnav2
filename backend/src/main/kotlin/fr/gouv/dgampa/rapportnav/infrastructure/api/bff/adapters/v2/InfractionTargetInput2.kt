package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VehicleTypeEnum

data class InfractionTargetInput2 (
    val id: String? = null,
    val vesselIdentifier: String? = null,
    val vesselType: String? = null,
    val vesselSize: String? = null,
    val identityControlledPerson: String,
    val vehicleType: VehicleTypeEnum? = null,
)
