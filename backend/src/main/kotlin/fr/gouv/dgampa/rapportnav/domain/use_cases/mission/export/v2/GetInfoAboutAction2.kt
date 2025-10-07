package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import kotlin.time.DurationUnit

@UseCase
class GetInfoAboutNavAction2(
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
                actions?.filterIsInstance<MissionFishActionEntity>()?.filter {
                    val actionType = mappedActionTypes[it.fishActionType]
                    actionType in actionTypes
                }
            }

            MissionSourceEnum.MONITORENV -> {
                val mappedActionTypes = mapOf(
                    ActionTypeEnum.CONTROL to ActionType.CONTROL,
                    ActionTypeEnum.SURVEILLANCE to ActionType.SURVEILLANCE,
                )
                actions?.filterIsInstance<MissionEnvActionEntity>()?.filter {
                    val actionType = mappedActionTypes[it.envActionType]
                    actionType in actionTypes
                }
            }

            MissionSourceEnum.RAPPORTNAV -> {
                actions?.filterIsInstance<MissionNavActionEntity>()
                    ?.filter { it.actionType in actionTypes }
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
        val durationInSeconds = actions?.filterIsInstance<MissionEnvActionEntity>()?.map {
            computeDurations.durationInSeconds(
                it.startDateTimeUtc,
                it.endDateTimeUtc
            ) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getAccumulatedDurationInHoursForNavAction(actions: List<MissionActionEntity>?): Double {
        val durationInSeconds = actions?.filterIsInstance<MissionNavActionEntity>()?.map {
            computeDurations.durationInSeconds(it.startDateTimeUtc, it.endDateTimeUtc) ?: 0
        }?.reduceOrNull { acc, duration -> acc + duration } ?: 0

        return computeDurations.convertFromSeconds(durationInSeconds, DurationUnit.HOURS)
    }

    private fun getCount(actions: List<MissionActionEntity>?): Int {
        return actions?.count() ?: 0
    }

}
