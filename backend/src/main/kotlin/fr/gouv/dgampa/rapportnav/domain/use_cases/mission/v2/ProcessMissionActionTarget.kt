package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository

@UseCase
class ProcessMissionActionTarget(
    private val targetRepo: ITargetRepository
) {

    fun execute(actionId: String, targets: List<TargetEntity2>): List<TargetEntity2>? {
        val targetIds = targets.map { it.id }
        val databaseTargets = targetRepo.findByActionId(actionId)
            .map { TargetEntity2.fromTargetModel(it) }

        val toDeleteInfractions = databaseTargets.filter { !targetIds.contains(it.id) }
        val toSaveInfractions = targets.filter { !databaseTargets.contains(it) }

        delete(toDeleteInfractions)
        return save(toSaveInfractions)
    }

    fun save(targets: List<TargetEntity2>?): List<TargetEntity2>? {
        return targets?.map { TargetEntity2.fromTargetModel(targetRepo.save(it.toTargetModel())) }
    }

    fun delete(targets: List<TargetEntity2>?) {
        targets?.forEach { targetRepo.deleteById(it.id) }
    }

}
