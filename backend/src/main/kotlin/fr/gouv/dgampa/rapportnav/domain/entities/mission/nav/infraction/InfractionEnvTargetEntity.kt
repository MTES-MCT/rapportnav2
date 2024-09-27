package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import java.util.*

data class InfractionEnvTargetEntity(
    var id: UUID,
    var missionId: Int,
    var actionId: String,
    var infractionId: UUID,
    val identityControlledPerson: String,
    var vesselType: VesselTypeEnum? = null,
    var vesselSize: VesselSizeEnum? = null,
    val vesselIdentifier: String? =  null,

)
