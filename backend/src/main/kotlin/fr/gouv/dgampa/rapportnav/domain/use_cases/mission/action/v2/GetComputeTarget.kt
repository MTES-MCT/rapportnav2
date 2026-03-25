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
        var targets = targetRepo.findByActionId(actionId)
        if (targets.isEmpty()) {
            val target = save(getNewTarget(actionId = actionId))
            targets = listOf(target)
        }
        return targets.map { TargetEntity.fromTargetModel(it) }
    }

    fun save(target: TargetModel): TargetModel {
        return targetRepo.save(target)
    }

    private fun getNewTarget(actionId: String): TargetModel {
        return TargetModel(
            actionId = actionId,
            id = UUID.randomUUID(),
            controls = getNewControls(),
            targetType = TargetType.DEFAULT,
            startDateTimeUtc = Instant.now(),
            status = TargetStatusType.IN_PROCESS.toString(),
            source = MissionSourceEnum.RAPPORT_NAV.toString()
        )
    }

    private fun getNewControls(): List<ControlModel> {
        val controlTypes = listOf(
            ControlType.SECURITY,
            ControlType.NAVIGATION,
            ControlType.GENS_DE_MER,
            ControlType.ADMINISTRATIVE
        )
        return controlTypes.map {
            ControlModel(
                controlType = it,
                id = UUID.randomUUID(),
                amountOfControls = 0
            )
        }
    }
}
