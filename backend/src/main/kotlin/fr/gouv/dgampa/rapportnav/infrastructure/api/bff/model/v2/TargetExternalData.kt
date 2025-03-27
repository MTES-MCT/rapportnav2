package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetExternalDataEntity

class TargetExternalData(
    val id: String,
    val natinfs: List<String>? = listOf(),
    val observations: String? = null,
    val registrationNumber: String? = null,
    val companyName: String? = null,
    val relevantCourt: String? = null,
    val infractionType: InfractionTypeEnum? = null,
    val formalNotice: FormalNoticeEnum? = null,
    val toProcess: Boolean? = null,
    val controlledPersonIdentity: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
) {
    fun toTargetExternalDataEntity(): TargetExternalDataEntity {
        return TargetExternalDataEntity(
            id = id,
            natinfs = natinfs,
            toProcess = toProcess,
            vesselType = vesselType,
            vesselSize = vesselSize,
            companyName = companyName,
            formalNotice = formalNotice,
            observations = observations,
            relevantCourt = relevantCourt,
            infractionType = infractionType,
            registrationNumber = registrationNumber,
            controlledPersonIdentity = controlledPersonIdentity
        )
    }

    companion object {
        fun fromTargetExternalDataEntity(entity: TargetExternalDataEntity): TargetExternalData {
            return TargetExternalData(
                id = entity.id,
                natinfs = entity.natinfs,
                toProcess = entity.toProcess,
                vesselType = entity.vesselType,
                vesselSize = entity.vesselSize,
                companyName = entity.companyName,
                formalNotice = entity.formalNotice,
                observations = entity.observations,
                relevantCourt = entity.relevantCourt,
                infractionType = entity.infractionType,
                registrationNumber = entity.registrationNumber,
                controlledPersonIdentity = entity.controlledPersonIdentity
            )
        }
    }
}
