package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository

@UseCase
class ProcessMissionActionInfraction(
    private val infractionRepo: IInfractionRepository,
) {

    fun execute(actionId: String, control: ActionControlEntity): List<InfractionEntity>? {
        control.computeInfractionControlId()
        val toSaveInfractions = control.getControlInfractions()
        val infractionIds = toSaveInfractions.map { it.id }
        val controlIds = toSaveInfractions.map { it.controlId }

        val databaseInfractions = infractionRepo
            .findAllByActionId(actionId)
            .map { it.toInfractionEntity() }
            .filter { controlIds.contains(it.controlId) }

        val toDeleteInfractions = databaseInfractions.filter { !infractionIds.contains(it.id) }

        delete(toDeleteInfractions)
        return save(toSaveInfractions) //TODO: is not equals save
    }

    fun save(infractions: List<InfractionEntity>?): List<InfractionEntity>? {
        return infractions?.map { infractionRepo.save(it).toInfractionEntity() }
    }

    fun delete(infractions: List<InfractionEntity>?) {
        infractions?.forEach { infractionRepo.deleteById(it.id) }
    }

}
