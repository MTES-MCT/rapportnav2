package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.*

data class MissionEntity(
    val id: Int? = null,
    val idUUID: UUID? = null,
    val data: MissionEnvEntity? = null,
    val actions: List<MissionActionEntity>? = listOf(),
    val generalInfos: MissionGeneralInfoEntity2? = null
) {
    private val logger = LoggerFactory.getLogger(MissionEntity::class.java)

    fun isCompleteForStats(): CompletenessForStatsEntity {
        val actionsCompleteForStats = this.isActionsCompleteForStats()
        val envDataCompleteForStats = this.isEnvDataCompleteForStats()
        val generalInfoCompleteForStat = this.isGeneralInfoCompleteForStats()
        val isCompleteForStats = envDataCompleteForStats == true && generalInfoCompleteForStat == true

        return CompletenessForStatsEntity(
            status = if (isCompleteForStats) actionsCompleteForStats.status
            else CompletenessForStatsStatusEnum.INCOMPLETE,
            sources = if (isCompleteForStats) actionsCompleteForStats.sources else actionsCompleteForStats.sources?.plus(
                MissionSourceEnum.RAPPORT_NAV
            )
                ?.distinct(),
        )
    }

    fun calculateMissionStatus(
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
    ): MissionStatusEnum {
        val compareDate = Instant.now()
        if (endDateTimeUtc == null || startDateTimeUtc == null) return MissionStatusEnum.UNAVAILABLE
        val endDateTime = Instant.parse(endDateTimeUtc.toString())
        val startDateTime = Instant.parse(startDateTimeUtc.toString())
        try {
            if (startDateTime.isBefore(compareDate) && endDateTime.isAfter(compareDate)) return MissionStatusEnum.IN_PROGRESS
            if (endDateTime.isBefore(compareDate) || endDateTime.equals(compareDate)) return MissionStatusEnum.ENDED
            if (startDateTime.isAfter(compareDate) || startDateTime.equals(compareDate))
                return MissionStatusEnum.UPCOMING
        } catch (e: DateTimeParseException) {
            logger.error("calculateMissionStatus - error with startDate: ${startDateTime}, endDate: ${endDateTime}", e)
        }
        return MissionStatusEnum.UNAVAILABLE
    }

    private fun isActionsCompleteForStats(): CompletenessForStatsEntity {

        val sources: List<MissionSourceEnum> = this.actions
            ?.filter { it.isCompleteForStats != true }
            ?.flatMap { it.sourcesOfMissingDataForStats.orEmpty() }
            ?.distinct()
            ?: emptyList()

        val worstStatus = when {
            this.actions?.any { it.completenessForStats?.status == CompletenessForStatsStatusEnum.INCOMPLETE } == true ->
                CompletenessForStatsStatusEnum.INCOMPLETE
            this.actions?.any { it.completenessForStats?.status == CompletenessForStatsStatusEnum.INVALID } == true ->
                CompletenessForStatsStatusEnum.INVALID
            else -> CompletenessForStatsStatusEnum.VALID
        }

        return CompletenessForStatsEntity(
            status = worstStatus,
            sources = sources.distinct()
        )
    }

    private fun isEnvDataCompleteForStats(): Boolean? {
        // don't validate for NavMissions (i.e. when IdUUID)
        if (data?.idUUID != null) return true
        return data?.isCompleteForStats(
            serviceTypeEnum = generalInfos?.serviceType,
            isResourcesNotUsed = generalInfos?.data?.isResourcesNotUsed
        )
    }

    private fun isGeneralInfoCompleteForStats(): Boolean? {
        // for secondary missions (missions conjointes ou inter-services)
        // do not validate generalInfos, only actions matter
        return if (this.isInterServices()) {
            true
        } else {
            this.generalInfos?.isCompleteForStats()
        }
    }

    // interservices means with other controlUnits
    private fun isInterServices(): Boolean =
        data?.controlUnits
            .orEmpty()
            .distinct()
            .take(2)     // stops early if more than 1 distinct value
            .count() == 2

    /**
     * Per-source recap of the mission's actions for the ULAM list expanded row.
     * Counts 1 per action (env's `actionNumberOfControls` is intentionally ignored) and emits one
     * [MissionActionSummaryEntity] per source that has at least one action.
     *
     * Classification:
     *  - RAPPORT_NAV: controls via [MissionActionEntity.isControl] (includes INQUIRY), surveillances via
     *    [NAV_SURVEILLANCE_TYPES], everything else -> other.
     *  - MONITORENV: by `envActionType` (CONTROL / SURVEILLANCE / NOTE -> other).
     *  - MONITORFISH: by `fishActionType` (SEA/LAND/AIR_CONTROL -> control, AIR_SURVEILLANCE -> surveillance,
     *    OBSERVATION -> other).
     */
    fun computeActionsSummary(): List<MissionActionSummaryEntity> {
        val summaries = mutableListOf<MissionActionSummaryEntity>()

        actions?.filterIsInstance<MissionNavActionEntity>()?.takeIf { it.isNotEmpty() }?.let { navActions ->
            summaries.add(
                MissionActionSummaryEntity(
                    source = MissionSourceEnum.RAPPORT_NAV,
                    nbControls = navActions.count { it.isControl() },
                    nbSurveillances = navActions.count { NAV_SURVEILLANCE_TYPES.contains(it.actionType) },
                    nbOtherActions = navActions.count {
                        !it.isControl() && !NAV_SURVEILLANCE_TYPES.contains(it.actionType)
                    },
                )
            )
        }

        actions?.filterIsInstance<MissionEnvActionEntity>()?.takeIf { it.isNotEmpty() }?.let { envActions ->
            summaries.add(
                MissionActionSummaryEntity(
                    source = MissionSourceEnum.MONITORENV,
                    nbControls = envActions.count { it.envActionType == ActionTypeEnum.CONTROL },
                    nbSurveillances = envActions.count { it.envActionType == ActionTypeEnum.SURVEILLANCE },
                    nbOtherActions = envActions.count {
                        it.envActionType != ActionTypeEnum.CONTROL && it.envActionType != ActionTypeEnum.SURVEILLANCE
                    },
                )
            )
        }

        actions?.filterIsInstance<MissionFishActionEntity>()?.takeIf { it.isNotEmpty() }?.let { fishActions ->
            summaries.add(
                MissionActionSummaryEntity(
                    source = MissionSourceEnum.MONITORFISH,
                    nbControls = fishActions.count { FISH_CONTROL_TYPES.contains(it.fishActionType) },
                    nbSurveillances = fishActions.count { it.fishActionType == MissionActionType.AIR_SURVEILLANCE },
                    nbOtherActions = fishActions.count { it.fishActionType == MissionActionType.OBSERVATION },
                )
            )
        }

        return summaries
    }

    companion object {
        // ULAM timeline surveillance group (see use-ulam-timeline-registry.tsx). Nav controls are classified
        // via MissionActionEntity.isControl() instead of a set here.
        private val NAV_SURVEILLANCE_TYPES = setOf(
            ActionType.NAUTICAL_EVENT,
            ActionType.LAND_SURVEILLANCE,
            ActionType.MARITIME_SURVEILLANCE,
        )

        private val FISH_CONTROL_TYPES = setOf(
            MissionActionType.SEA_CONTROL,
            MissionActionType.LAND_CONTROL,
            MissionActionType.AIR_CONTROL,
        )
    }
}

