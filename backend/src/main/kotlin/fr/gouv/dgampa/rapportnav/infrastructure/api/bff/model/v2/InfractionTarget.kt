package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity

data class InfractionTarget (
    val id: String? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
    val identityControlledPerson: String? = null,
    val vehicleType: VehicleTypeEnum? = null,
    val natinfs: List<Any>? = listOf(),
    val observations: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum? = null,
    val formalNotice: FormalNoticeEnum? = null,
    val toProcess: Boolean? = null,
) {
    companion object {
        fun fromInfractionEntity(target: InfractionEnvTargetEntity?): InfractionTarget {
            return InfractionTarget(
                id = target?.id?.toString(),
                vesselIdentifier = target?.vesselIdentifier,
                vesselType = target?.vesselType,
                vesselSize = target?.vesselSize,
                identityControlledPerson = target?.identityControlledPerson
            )
        }

        fun fromInfractionEnvEntity(target: fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity?): InfractionTarget {
            return InfractionTarget(
                id = target?.id,
                natinfs = null,
                companyName = target?.companyName,
                relevantCourt = target?.relevantCourt,
                infractionType = target?.infractionType,
                formalNotice = target?.formalNotice,
                toProcess = target?.toProcess,
                observations = target?.observations,
                vesselType = target?.vesselType,
                vesselSize = target?.vesselSize,
                vesselIdentifier = target?.registrationNumber,
                identityControlledPerson = target?.controlledPersonIdentity,
            )
        }
    }
}
