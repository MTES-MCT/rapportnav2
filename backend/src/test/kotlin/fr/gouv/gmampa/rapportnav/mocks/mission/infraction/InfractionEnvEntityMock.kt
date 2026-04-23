package fr.gouv.gmampa.rapportnav.mocks.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import java.util.*

object InfractionEnvEntityMock {

    fun create(
        id: String = UUID.randomUUID().toString(),
        natinf: List<String>? = listOf(),
        observations: String? = null,
        registrationNumber: String? = null,
        companyName: String? = null,
        relevantCourt: String? = null,
        infractionType: InfractionTypeEnum = InfractionTypeEnum.WITH_REPORT,
        formalNotice: FormalNoticeEnum = FormalNoticeEnum.YES,
        controlledPersonIdentity: String? = null,
        vesselType: String? = null,
        vesselSize: Number? = null,
    ): InfractionEnvEntity {
        return InfractionEnvEntity(
            id = id,
            natinf = natinf,
            registrationNumber = registrationNumber,
            companyName = companyName,
            relevantCourt = relevantCourt,
            infractionType = infractionType,
            formalNotice = formalNotice,
            observations = observations,
            controlledPersonIdentity = controlledPersonIdentity,
            vesselType = vesselType,
            vesselSize = vesselSize,
        )
    }
}
