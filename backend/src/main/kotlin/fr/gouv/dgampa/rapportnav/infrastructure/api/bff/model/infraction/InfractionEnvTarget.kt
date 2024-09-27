package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import java.util.*

data class InfractionEnvTarget(
    var id: UUID,
    var vesselType: VesselTypeEnum? = null,
    var vesselSize: VesselSizeEnum? = null,
    val vesselIdentifier: String? = null,
    val identityControlledPerson: String,
) {
    companion object {
        fun fromEnvInfractionEntity(infractionEntity: InfractionEnvTargetEntity? = null) = infractionEntity?.let {
            InfractionEnvTarget(
                id = it.id,
                vesselIdentifier = infractionEntity.vesselIdentifier,
                vesselType = infractionEntity.vesselType,
                vesselSize = infractionEntity.vesselSize,
                identityControlledPerson = infractionEntity.identityControlledPerson
            )
        }
    }
}
