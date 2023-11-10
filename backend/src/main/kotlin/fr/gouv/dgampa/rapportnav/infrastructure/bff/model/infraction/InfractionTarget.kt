package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity


data class InfractionTarget(
    val id: String,
//    val natinf: List<String>? = listOf(),
    val natinfs: List<Any>? = listOf(),
    val observations: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum? = null,
    val formalNotice: FormalNoticeEnum? = null,
    val toProcess: Boolean? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
    val vesselIdentifier: String? = null,
    val identityControlledPerson: String? = null,
) {
    companion object {
        fun fromInfractionEntity(infractionEntity: InfractionEntity) = InfractionTarget(
            id = infractionEntity.id.toString(),
//            natinf = infractionEntity.natinf,
            natinfs = null,
            observations = infractionEntity.observations,
            vesselIdentifier = infractionEntity.target?.vesselIdentifier,
            identityControlledPerson = infractionEntity.target?.identityControlledPerson,
            vesselType = infractionEntity.target?.vesselType,
            vesselSize = infractionEntity.target?.vesselSize,
        )

        fun fromEnvInfractionEntity(infractionEntity: fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEntity) = InfractionTarget(
            id = infractionEntity.id,
//            natinf = infractionEntity.natinf,
            natinfs = null,
            observations = infractionEntity.observations,
            companyName = infractionEntity.companyName,
            relevantCourt = infractionEntity.relevantCourt,
            infractionType = infractionEntity.infractionType,
            formalNotice = infractionEntity.formalNotice,
            toProcess = infractionEntity.toProcess,
            vesselIdentifier = infractionEntity.registrationNumber,
            identityControlledPerson = infractionEntity.controlledPersonIdentity,
            vesselType = infractionEntity.vesselType,
            vesselSize = infractionEntity.vesselSize,
        )
    }
}
