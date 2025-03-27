package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.target2.v2.TargetType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ITargetRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.v2.ControlModel2
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import org.slf4j.LoggerFactory
import java.util.*

@UseCase
class GetComputeTarget(
    private val targetRepo: ITargetRepository
) {
    private val logger = LoggerFactory.getLogger(GetComputeTarget::class.java)
    fun execute(actionId: String, isControl: Boolean?): List<TargetEntity2>? {
        if (isControl != true) return null
        return try {
            var targets = targetRepo.findByActionId(actionId)
            if (targets.isEmpty()) {
                val target = save(getNewTarget(actionId = actionId))
                targets = listOf(target)
            }
            targets.map { TargetEntity2.fromTargetModel(it) }
        } catch (e: Exception) {
            logger.error("ComputeEnvTarget failed loading targets", e)
            return listOf()
        }
    }

    fun save(target: TargetModel2): TargetModel2 {
        return targetRepo.save(target)
    }

    private fun getNewTarget(actionId: String): TargetModel2 {
        return TargetModel2(
            actionId = actionId,
            id = UUID.randomUUID(),
            controls = getNewControls(),
            targetType = TargetType.DEFAULT,
            status = TargetStatusType.IN_PROCESS.toString(),
            source = MissionSourceEnum.RAPPORTNAV.toString(),
        )
    }

    private fun getNewControls(): List<ControlModel2> {
        val controlTypes = listOf(
            ControlType.SECURITY,
            ControlType.NAVIGATION,
            ControlType.GENS_DE_MER,
            ControlType.ADMINISTRATIVE
        )
        return controlTypes.map { ControlModel2(controlType = it, id = UUID.randomUUID(), amountOfControls = 0) }
    }
}
