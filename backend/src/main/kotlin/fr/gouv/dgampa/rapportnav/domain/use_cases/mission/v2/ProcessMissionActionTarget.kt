package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository

@UseCase
class ProcessMissionActionTarget(
    private val targetRepo: ITargetRepository
) {

    fun execute(actionId: String, targets: List<TargetEntity>): List<TargetEntity>? {
        validateInfractionTypes(targets)
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

    /**
     * An infraction must carry a report type (Rappel / Verbalisation / En attente) — the DB column
     * infraction_2.infraction_type is NOT NULL and, more importantly, the type is a legal enforcement
     * outcome that must be chosen, never inferred. A null here means the client sent an incomplete
     * infraction, so reject it as a usage error (400) instead of letting it surface as a DB 500.
     */
    private fun validateInfractionTypes(targets: List<TargetEntity>) {
        val hasUntypedInfraction = targets
            .flatMap { it.controls ?: emptyList() }
            .flatMap { it.infractions ?: emptyList() }
            .any { it.infractionType == null }

        if (hasUntypedInfraction) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "Chaque infraction doit préciser une action mise en œuvre (infractionType)"
            )
        }
    }

}
