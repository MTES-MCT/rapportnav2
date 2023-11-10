package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*

data class InfractionEnvData(
    val id: String,
    val natinf: List<String>? = listOf(),
    val observations: String? = null,
    val registrationNumber: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum,
    val formalNotice: FormalNoticeEnum,
    val toProcess: Boolean,
    val controlledPersonIdentity: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
) {
    companion object {
        fun fromEnvInfractionEntity(infractionEntity: InfractionEntity) = InfractionEnvData(
            id = infractionEntity.id,
            natinf = infractionEntity.natinf,
            observations = infractionEntity.observations,
            registrationNumber = infractionEntity.registrationNumber,
            companyName = infractionEntity.companyName,
            relevantCourt = infractionEntity.relevantCourt,
            infractionType = infractionEntity.infractionType,
            formalNotice = infractionEntity.formalNotice,
            toProcess = infractionEntity.toProcess,
            controlledPersonIdentity = infractionEntity.controlledPersonIdentity,
            vesselType = infractionEntity.vesselType,
            vesselSize = infractionEntity.vesselSize,
        )
    }
}
