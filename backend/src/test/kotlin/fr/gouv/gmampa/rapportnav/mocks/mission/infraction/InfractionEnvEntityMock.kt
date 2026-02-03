package fr.gouv.gmampa.rapportnav.mocks.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.*
import java.util.*

object EnvInfractionEntityMock {

    fun create(
        id: String = UUID.randomUUID().toString(),
        natinf: List<String>? = listOf(),
        observations: String? = null,
        registrationNumber: String? = null,
        companyName: String? = null,
        relevantCourt: String? = null,
        infractionType: InfractionTypeEnum = InfractionTypeEnum.WITH_REPORT,
        formalNotice: FormalNoticeEnum = FormalNoticeEnum.YES,
        toProcess: Boolean = false,
        controlledPersonIdentity: String? = null,
        vesselType: VesselTypeEnum? = null,
        vesselSize: VesselSizeEnum? = null,
    ): InfractionEnvEntity {
        return InfractionEnvEntity(
            id = id,
            natinf = natinf,
            registrationNumber = registrationNumber,
            companyName = companyName,
            relevantCourt = relevantCourt,
            infractionType = infractionType,
            formalNotice = formalNotice,
            toProcess = toProcess,
            observations = observations,
            controlledPersonIdentity = controlledPersonIdentity,
            vesselType = vesselType,
            vesselSize = vesselSize,
        )
    }
}
