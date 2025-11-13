package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.controlPolicies

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ControlPolicyData
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity2
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.TargetEntity2
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import org.slf4j.LoggerFactory

@UseCase
class ComputeNavControlPolicy {

    private val logger = LoggerFactory.getLogger(ComputeNavControlPolicy::class.java)
    private val countInfractions = CountInfractions()

    fun execute(mission: MissionEntity2?, controlType: ControlType): ControlPolicyData? {
        if (mission == null) {
            logger.warn("ComputeNavControlPolicy: Mission is null — skipping computation.")
            return null
        }

        val actions = mission.actions.orEmpty()
        if (actions.isEmpty()) {
            logger.info("ComputeNavControlPolicy: Mission ${mission.id} has no actions.")
            return ControlPolicyData()
        }

        try {
            // Compute all filtered actions once
            val filteredActions = filterControls(
                actions = actions,
                controlType = controlType,
                navControlMethods = listOf(ControlMethod.SEA, ControlMethod.LAND),
                fishActionTypes = listOf(MissionActionType.SEA_CONTROL, MissionActionType.LAND_CONTROL)
            )

            val (seaActions, landActions) = filteredActions.partition { action ->
                when (action) {
                    is MissionNavActionEntity -> action.controlMethod == ControlMethod.SEA
                    is MissionFishActionEntity -> action.fishActionType == MissionActionType.SEA_CONTROL
                    else -> false
                }
            }

            val nbInfractionsWithRecord = countInfractions.countNavInfractions(
                actions = filteredActions,
                controlType = controlType,
                infractionType = InfractionTypeEnum.WITH_REPORT
            )

            val nbInfractionsWithoutRecord = countInfractions.countNavInfractions(
                actions = filteredActions,
                controlType = controlType,
                infractionType = InfractionTypeEnum.WITHOUT_REPORT
            )

            return ControlPolicyData(
                nbControls = filteredActions.size,
                nbControlsSea = seaActions.size,
                nbControlsLand = landActions.size,
                nbInfractionsWithRecord = nbInfractionsWithRecord,
                nbInfractionsWithoutRecord = nbInfractionsWithoutRecord
            )
        } catch (e: Exception) {
            logger.error("ComputeNavControlPolicy: Failed to compute for mission ${mission.id}", e)
            return null
        }
    }

    private fun filterControls(
        actions: List<MissionActionEntity>,
        controlType: ControlType,
        navControlMethods: List<ControlMethod>,
        fishActionTypes: List<MissionActionType>
    ): List<MissionActionEntity> {
        if (actions.isEmpty()) return emptyList()

        return actions.filter { action ->
            try {
                when (action) {
                    is MissionNavActionEntity ->
                        action.actionType == ActionType.CONTROL &&
                            action.controlMethod in navControlMethods &&
                            hasMatchingControl(action.targets, controlType)

                    is MissionFishActionEntity ->
                        action.fishActionType in fishActionTypes &&
                            hasMatchingControl(action.targets, controlType)

                    is MissionEnvActionEntity ->
                        action.envActionType == ActionTypeEnum.CONTROL &&
                            hasMatchingControl(action.targets, controlType)

                    else -> false
                }
            } catch (e: Exception) {
                logger.warn("ComputeNavControlPolicy: Skipping invalid action", e)
                false
            }
        }
    }

    private fun hasMatchingControl(
        targets: List<TargetEntity2>?,
        controlType: ControlType
    ): Boolean {
        return targets.orEmpty().any { target ->
            target.controls.orEmpty().any { control ->
                control.controlType == controlType && control.amountOfControls > 0
            }
        }
    }
}
