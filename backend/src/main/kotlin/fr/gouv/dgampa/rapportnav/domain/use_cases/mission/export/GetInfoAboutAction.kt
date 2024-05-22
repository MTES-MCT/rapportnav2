package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import kotlin.time.DurationUnit

@UseCase
class GetInfoAboutNavAction(
    private val computeDurations: ComputeDurations
) {

    fun execute(
        actions: List<MissionActionEntity>?,
        actionTypes: List<ActionType>
    ): NavActionInfoEntity? {
        return actions?.takeIf { it.isNotEmpty() }?.let { nonNullActions ->
            actionTypes.takeIf { it.isNotEmpty() }?.let {
                val navActionsForActionTypes =
                    this.filterNavActionPerType(actions = nonNullActions, actionTypes = actionTypes)
                NavActionInfoEntity(
                    count = this.getCount(navActionsForActionTypes),
                    durationInHours = this.getAccumulatedDurationInHours(navActionsForActionTypes)
                )
            }
        }
    }


    private fun filterNavActionPerType(
        actions: List<MissionActionEntity>?,
        actionTypes: List<ActionType>
    ): List<MissionActionEntity.NavAction>? {
        return actions?.filterIsInstance<MissionActionEntity.NavAction>()
            ?.filter { it.navAction.actionType in actionTypes }
    }

    private fun getAccumulatedDurationInHours(actions: List<MissionActionEntity.NavAction>?): Double {
        val durationInSeconds = actions?.map {
            computeDurations.durationInSeconds(it.navAction.startDateTimeUtc, it.navAction.endDateTimeUtc) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getCount(actions: List<MissionActionEntity.NavAction>?): Int {
        return actions?.count() ?: 0
    }

}
