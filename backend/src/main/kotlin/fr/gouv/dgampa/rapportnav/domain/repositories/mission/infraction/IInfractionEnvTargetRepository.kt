package fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionEnvTargetModel

interface IInfractionEnvTargetRepository {
    fun save(infractionTarget: InfractionEnvTargetEntity, infraction: InfractionEntity): InfractionEnvTargetModel

    fun findByActionIdAndVesselIdentifier(actionId: String, vesselIdentifier: String): List<InfractionEnvTargetModel>
}
