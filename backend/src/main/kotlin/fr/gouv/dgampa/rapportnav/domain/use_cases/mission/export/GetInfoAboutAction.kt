package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
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
        actionTypes: List<ActionType>,
        actionSource: MissionSourceEnum
    ): NavActionInfoEntity? {
        return actions?.takeIf { it.isNotEmpty() }?.let { nonNullActions ->
            actionTypes.takeIf { it.isNotEmpty() }?.let {
                val filteredActions =
                    this.filterActionPerType(
                        actions = nonNullActions,
                        actionTypes = actionTypes,
                        actionSource = actionSource
                    )
                val count = this.getCount(filteredActions)
                val durationInHours = this.getAccumulatedDurationInHours(filteredActions, actionSource)
                NavActionInfoEntity(
                    count = if (count == 0) null else count,
                    durationInHours = if (durationInHours == 0.0) null else durationInHours
                )
            }
        }
    }


    private fun filterActionPerType(
        actions: List<MissionActionEntity>?,
        actionTypes: List<ActionType>,
        actionSource: MissionSourceEnum
    ): List<MissionActionEntity>? {

        val filteredActions = when (actionSource) {
            MissionSourceEnum.MONITORFISH -> {
                val mappedActionTypes = mapOf(
                    MissionActionType.LAND_CONTROL to ActionType.CONTROL,
                    MissionActionType.SEA_CONTROL to ActionType.CONTROL,
                    MissionActionType.AIR_CONTROL to ActionType.CONTROL,
                    MissionActionType.AIR_SURVEILLANCE to ActionType.SURVEILLANCE,
                )
                actions?.filterIsInstance<MissionActionEntity.FishAction>()?.filter {
                    val actionType = mappedActionTypes[it.fishAction.controlAction?.action?.actionType]
                    actionType in actionTypes
                }
            }

            MissionSourceEnum.MONITORENV -> {
                actions?.filterIsInstance<MissionActionEntity.EnvAction>()?.filter {
                    (it.envAction?.controlAction != null && ActionType.CONTROL in actionTypes) ||
                        (it.envAction?.surveillanceAction != null && ActionType.SURVEILLANCE in actionTypes)
                }
            }

            MissionSourceEnum.RAPPORTNAV -> {
                actions?.filterIsInstance<MissionActionEntity.NavAction>()
                    ?.filter { it.navAction.actionType in actionTypes }
            }

            else -> null
        }

        return filteredActions
    }

    private fun getAccumulatedDurationInHours(
        actions: List<MissionActionEntity>?,
        actionSource: MissionSourceEnum
    ): Double? {
        return when (actionSource) {
            MissionSourceEnum.RAPPORTNAV -> getAccumulatedDurationInHoursForNavAction(actions = actions)
            MissionSourceEnum.MONITORENV -> getAccumulatedDurationInHoursForEnvAction(actions = actions)
            else -> null
        }
    }

    private fun getAccumulatedDurationInHoursForEnvAction(actions: List<MissionActionEntity>?): Double {
        val durationInSeconds = actions?.filterIsInstance<MissionActionEntity.EnvAction>()?.map {
            if (it.envAction?.controlAction != null) {
                computeDurations.durationInSeconds(
                    it.envAction.controlAction.action?.actionStartDateTimeUtc,
                    it.envAction.controlAction.action?.actionEndDateTimeUtc
                ) ?: 0
            } else if (it.envAction?.surveillanceAction != null) {
                computeDurations.durationInSeconds(
                    it.envAction.surveillanceAction.action.actionStartDateTimeUtc,
                    it.envAction.surveillanceAction.action.actionEndDateTimeUtc
                ) ?: 0
            } else {
                0
            }
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getAccumulatedDurationInHoursForNavAction(actions: List<MissionActionEntity>?): Double {
        val durationInSeconds = actions?.filterIsInstance<MissionActionEntity.NavAction>()?.map {
            computeDurations.durationInSeconds(it.navAction.startDateTimeUtc, it.navAction.endDateTimeUtc) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getCount(actions: List<MissionActionEntity>?): Int {
        return actions?.count() ?: 0
    }

}
