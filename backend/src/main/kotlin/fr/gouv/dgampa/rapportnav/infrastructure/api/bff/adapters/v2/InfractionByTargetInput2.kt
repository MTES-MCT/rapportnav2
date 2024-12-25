package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType

data class InfractionByTargetInput2(
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val controlTypesWithInfraction: List<ControlType>? = listOf(),
    val targetAddedByUnit: Boolean? = null,
    val infractions: List<InfractionInput2> = listOf(),
    val identityControlledPerson: String?  = null
) {

}
