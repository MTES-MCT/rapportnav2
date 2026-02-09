package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.controlPolicies

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ControlPolicyData
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.helpers.CountInfractions
import org.slf4j.LoggerFactory
import kotlin.collections.filterIsInstance
import kotlin.collections.orEmpty

@UseCase
class ComputeProFishingControlPolicy(
    private val countInfractions: CountInfractions = CountInfractions() // injectable for testing
) {
    private val logger = LoggerFactory.getLogger(ComputeProFishingControlPolicy::class.java)

    private fun filterActions(mission: MissionEntity?): List<FishActionEntity> {
        if (mission == null) {
            logger.warn("filterActions called with null mission.")
            return emptyList()
        }

        val filtered = mission.actions
            .orEmpty()
            .filterIsInstance<FishActionEntity>()
            .filter {
                it.fishActionType == MissionActionType.SEA_CONTROL ||
                        it.fishActionType == MissionActionType.LAND_CONTROL
            }

        logger.debug("Filtered ${filtered.size} fishing actions out of ${mission.actions?.size ?: 0}")
        return filtered
    }

    fun computeFishingRelatedInfractions(mission: MissionEntity?): ControlPolicyData? {
        if (mission == null) {
            logger.warn("computeFishingRelatedInfractions called with null mission.")
            return null
        }

        val actions = filterActions(mission)
        if (actions.isEmpty()) {
            logger.info("No fishing control actions found for mission ${mission.id}.")
            return ControlPolicyData()
        }

        val withRecord = countInfractions.countFishInfractions(actions, InfractionType.WITH_RECORD)
        val withoutRecord = countInfractions.countFishInfractions(actions, InfractionType.WITHOUT_RECORD)

        return ControlPolicyData(
            nbControls = actions.size,
            nbControlsSea = actions.count { it.fishActionType == MissionActionType.SEA_CONTROL },
            nbControlsLand = actions.count { it.fishActionType == MissionActionType.LAND_CONTROL },
            nbInfractionsWithRecord = withRecord.values.sum(),
            nbInfractionsWithoutRecord = withoutRecord.values.sum()
        )
    }

    fun computeOtherInfractions(mission: MissionEntity?): ControlPolicyData? {
        if (mission == null) {
            logger.warn("computeOtherInfractions called with null mission.")
            return null
        }

        val actions = filterActions(mission)
        if (actions.isEmpty()) {
            logger.info("No fishing control actions found for mission ${mission.id}.")
            return ControlPolicyData()
        }

        return ControlPolicyData(
            nbControls = actions.size,
            nbControlsSea = actions.count { it.fishActionType == MissionActionType.SEA_CONTROL },
            nbControlsLand = actions.count { it.fishActionType == MissionActionType.LAND_CONTROL },
            nbInfractionsWithRecord = countInfractions.countOtherFishInfractions(actions, InfractionType.WITH_RECORD),
            nbInfractionsWithoutRecord = countInfractions.countOtherFishInfractions(actions, InfractionType.WITHOUT_RECORD)
        )
    }
}
