package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository

@UseCase
class AddOrUpdateInfractionEnvTarget(
    private val repo: IInfractionEnvTargetRepository,
) {
    fun execute(infractionTarget: InfractionEnvTargetEntity, infraction: InfractionEntity): InfractionEnvTargetEntity {
        val savedData = repo.save(infractionTarget, infraction).toInfractionEnvTargetEntity()
        return savedData
    }
}
