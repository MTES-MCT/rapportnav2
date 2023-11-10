package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import java.util.*

data class InfractionEnvTargetEntity(
    var id: UUID,
    var missionId: Int,
    var actionId: String,
    var infractionId: UUID,
    var vesselType: VesselTypeEnum,
    var vesselSize: VesselSizeEnum,
    val vesselIdentifier: String,
    val identityControlledPerson: String,
)
