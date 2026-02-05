package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository

@UseCase
class ProcessMissionActionTarget(
    private val targetRepo: ITargetRepository
) {

    fun execute(actionId: String, targets: List<TargetEntity>): List<TargetEntity>? {
        val targetIds = targets.map { it.id }
        val databaseTargets = targetRepo.findByActionId(actionId)
            .map { TargetEntity.fromTargetModel(it) }

        val toDeleteInfractions = databaseTargets.filter { !targetIds.contains(it.id) }
        val toSaveInfractions = targets.filter { !databaseTargets.contains(it) }

        delete(toDeleteInfractions)
        return save(toSaveInfractions)
    }

    fun save(targets: List<TargetEntity>?): List<TargetEntity>? {
        return targets?.map { TargetEntity.fromTargetModel(targetRepo.save(it.toTargetModel())) }
    }

    fun delete(targets: List<TargetEntity>?) {
        targets?.forEach { targetRepo.deleteById(it.id) }
    }

}
