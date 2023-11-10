package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import java.util.*

data class InfractionEnvTarget(
    var id: UUID,
    var vesselType: VesselTypeEnum,
    var vesselSize: VesselSizeEnum,
    val vesselIdentifier: String,
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
