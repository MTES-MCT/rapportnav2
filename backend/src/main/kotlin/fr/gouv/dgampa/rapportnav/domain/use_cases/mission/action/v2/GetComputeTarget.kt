package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import java.time.Instant
import java.util.*

@UseCase
class GetComputeTarget(
    private val targetRepo: ITargetRepository
) {
    fun execute(actionId: String, isControl: Boolean?): List<TargetEntity>? {
        if (isControl != true) return null
        val targets = targetRepo.findByActionId(actionId)
        // Do NOT persist on read. When no target exists yet, return an in-memory scaffold with a stable
        // (deterministic) id; it is persisted later on the next update through ProcessMissionActionTarget.
        // This keeps GET a pure read and stops reads from racing writes on target_2/control_2.
        val models = targets.ifEmpty { listOf(getNewTarget(actionId)) }
        return models.map { TargetEntity.fromTargetModel(it) }
    }

    private fun getNewTarget(actionId: String): TargetModel {
        val id = deterministicId(actionId)
        return TargetModel(
            actionId = actionId,
            id = id,
            controls = getNewControls(id),
            targetType = TargetType.DEFAULT,
            startDateTimeUtc = Instant.now(),
            status = TargetStatusType.IN_PROCESS.toString(),
            source = MissionSourceEnum.RAPPORT_NAV.toString()
        )
    }

    private fun getNewControls(targetId: UUID): List<ControlModel> {
        val controlTypes = listOf(
            ControlType.SECURITY,
            ControlType.NAVIGATION,
            ControlType.GENS_DE_MER,
            ControlType.ADMINISTRATIVE
        )
        return controlTypes.map {
            ControlModel(
                controlType = it,
                id = deterministicId("$targetId:$it"),
                amountOfControls = 0
            )
        }
    }

    /** Stable id derived from a seed so an unpersisted scaffold keeps the same id across reads. */
    private fun deterministicId(seed: String): UUID = UUID.nameUUIDFromBytes(seed.toByteArray())
}
