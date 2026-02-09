package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import kotlin.time.DurationUnit

@UseCase
class GetInfoAboutNavAction2(
    private val computeDurations: ComputeDurations
) {

    fun execute(
        actions: List<ActionEntity>?,
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
        actions: List<ActionEntity>?,
        actionTypes: List<ActionType>,
        actionSource: MissionSourceEnum
    ): List<ActionEntity>? {

        val filteredActions = when (actionSource) {
            MissionSourceEnum.MONITORFISH -> {
                val mappedActionTypes = mapOf(
                    MissionActionType.LAND_CONTROL to ActionType.CONTROL,
                    MissionActionType.SEA_CONTROL to ActionType.CONTROL,
                    MissionActionType.AIR_CONTROL to ActionType.CONTROL,
                    MissionActionType.AIR_SURVEILLANCE to ActionType.SURVEILLANCE,
                )
                actions?.filterIsInstance<FishActionEntity>()?.filter {
                    val actionType = mappedActionTypes[it.fishActionType]
                    actionType in actionTypes
                }
            }

            MissionSourceEnum.MONITORENV -> {
                val mappedActionTypes = mapOf(
                    ActionTypeEnum.CONTROL to ActionType.CONTROL,
                    ActionTypeEnum.SURVEILLANCE to ActionType.SURVEILLANCE,
                )
                actions?.filterIsInstance<EnvActionEntity>()?.filter {
                    val actionType = mappedActionTypes[it.envActionType]
                    actionType in actionTypes
                }
            }

            MissionSourceEnum.RAPPORTNAV -> {
                actions?.filterIsInstance<NavActionEntity>()
                    ?.filter { it.actionType in actionTypes }
            }

            else -> null
        }

        return filteredActions
    }

    private fun getAccumulatedDurationInHours(
        actions: List<ActionEntity>?,
        actionSource: MissionSourceEnum
    ): Double? {
        return when (actionSource) {
            MissionSourceEnum.RAPPORTNAV -> getAccumulatedDurationInHoursForNavAction(actions = actions)
            MissionSourceEnum.MONITORENV -> getAccumulatedDurationInHoursForEnvAction(actions = actions)
            else -> null
        }
    }

    private fun getAccumulatedDurationInHoursForEnvAction(actions: List<ActionEntity>?): Double {
        val durationInSeconds = actions?.filterIsInstance<EnvActionEntity>()?.map {
            computeDurations.durationInSeconds(
                it.startDateTimeUtc,
                it.endDateTimeUtc
            ) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getAccumulatedDurationInHoursForNavAction(actions: List<ActionEntity>?): Double {
        val durationInSeconds = actions?.filterIsInstance<NavActionEntity>()?.map {
            computeDurations.durationInSeconds(it.startDateTimeUtc, it.endDateTimeUtc) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getCount(actions: List<ActionEntity>?): Int {
        return actions?.count() ?: 0
    }

}
