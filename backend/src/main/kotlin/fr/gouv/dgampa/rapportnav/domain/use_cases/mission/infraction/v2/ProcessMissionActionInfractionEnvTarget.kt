package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionEnvTargetRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.InfractionInput2

@UseCase
class ProcessMissionActionInfractionEnvTarget(
    private val infractionRepo: IInfractionRepository,
    private val infractionEnvTargetRepo: IInfractionEnvTargetRepository,
) {

    fun execute(actionId: String, infractions: List<InfractionInput2>?): List<InfractionEntity>? {
        val infractionIds = infractions?.map { it.id } ?: listOf()

        val databaseInfractions = infractionRepo
            .findAllByActionId(actionId)
            .map { it.toInfractionEntity() }

        val toDeleteInfractions = databaseInfractions.filter { !infractionIds.contains(it.id.toString()) }

        delete(toDeleteInfractions)
        return save(infractions) //TODO: is not equals save
    }

    fun save(infractions: List<InfractionInput2>?): List<InfractionEntity>? {
        // check target / create or Update target
        // create infractionEntity  -- > save target
        //save infraction
        return infractions?.map {
            // todo check if it has changed
            val newTarget = it.toInfractionEnvTargetEntity()
            val entity = it.toInfractionEntity()
            entity.target = newTarget
            infractionRepo.save(entity).toInfractionEntity()
        }
    }

    fun delete(infractions: List<InfractionEntity>?) {
        infractions?.forEach { infractionRepo.deleteById(it.id) }
    }
}
