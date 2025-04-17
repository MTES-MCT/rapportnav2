package fr.gouv.gmampa.rapportnav.mocks.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import java.util.*

object InfractionEntityMock {

    fun create(
        id: UUID = UUID.randomUUID(),
        missionId: String = "1",
        actionId: String = "1234",
        controlId: UUID? = null,
        controlType: ControlType? = null,
        infractionType: InfractionTypeEnum? = null,
        natinfs: List<String>? = null,
        target: InfractionEnvTargetEntity? = null,
        observations: String? = null,
    ): InfractionEntity {
        return InfractionEntity(
            id = id,
            missionId = missionId,
            actionId = actionId,
            controlId = controlId,
            controlType = controlType,
            infractionType = infractionType,
            natinfs = natinfs,
            target = target,
            observations = observations,
        )
    }
}
