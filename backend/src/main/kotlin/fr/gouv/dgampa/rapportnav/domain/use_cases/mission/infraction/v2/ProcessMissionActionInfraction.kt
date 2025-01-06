package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.InfractionInput2

@UseCase
class ProcessMissionActionInfraction(
    private val infractionRepo: IInfractionRepository
) {

    fun execute(actionId: String, infractions: List<InfractionInput2>): List<InfractionEntity>? {
        val infractionIds = infractions.map { it.id }

        val databaseInfractions = infractionRepo
            .findAllByActionId(actionId)
            .map { it.toInfractionEntity() }
            .filter { it.controlId != null }

        val toDeleteInfractions = databaseInfractions.filter { !infractionIds.contains(it.id.toString()) }
        val toSaveInfractions = infractions.map { it.toInfractionEntity() }.filter { !databaseInfractions.contains(it) }

        delete(toDeleteInfractions)
        return save(toSaveInfractions)
    }

    fun save(infractions: List<InfractionEntity>?): List<InfractionEntity>? {
        return infractions?.map { infractionRepo.save(it).toInfractionEntity() }
    }

    fun delete(infractions: List<InfractionEntity>?) {
        infractions?.forEach { infractionRepo.deleteById(it.id) }
    }

}
