package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository

@UseCase
class ProcessMissionActionTarget(
    private val targetRepo: ITargetRepository
) {

    fun execute(actionId: String, targets: List<TargetEntity2>): List<TargetEntity2>? {
        return try {
            val targetIds = targets.map { it.id }
            val databaseTargets = targetRepo.findByActionId(actionId)
                .map { TargetEntity2.fromTargetModel(it) }

            val toDeleteTargets = databaseTargets.filter { !targetIds.contains(it.id) }
            val toSaveTargets = targets.filter { !databaseTargets.contains(it) }

            delete(toDeleteTargets)
            save(toSaveTargets)
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "ProcessMissionActionTarget failed for actionId=$actionId",
                originalException = e
            )
        }
    }

    private fun save(targets: List<TargetEntity2>?): List<TargetEntity2>? {
        return targets?.map { TargetEntity2.fromTargetModel(targetRepo.save(it.toTargetModel())) }
    }

    private fun delete(targets: List<TargetEntity2>?) {
        targets?.forEach { targetRepo.deleteById(it.id) }
    }
}
