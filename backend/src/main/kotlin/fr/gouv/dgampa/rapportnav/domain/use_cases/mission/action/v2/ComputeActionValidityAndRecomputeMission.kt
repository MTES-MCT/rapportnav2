package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionDates
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.RecomputeMissionValidation
import fr.gouv.dgampa.rapportnav.domain.validation.EntityValidityValidator
import fr.gouv.dgampa.rapportnav.domain.validation.ValidationPolicies
import java.util.UUID

/**
 * Shared write-path step for the action use cases: compute the action's completeness for the applicable
 * validation policy (so the nav mission_action row persists a correct `isCompleteForStats`), then recompute
 * the parent mission's validation.
 *
 * Two ordering constraints pull in opposite directions:
 *  - the action's own completeness must be computed BEFORE saving, because the nav row carries `isCompleteForStats`;
 *  - the mission recompute must run AFTER saving, because it re-reads the mission's actions from the DB — if it
 *    ran first it would aggregate the stale (pre-update) or missing (pre-create) row.
 *
 * Nav callers therefore split the work: [computeActionValidity] before the save, [recomputeMission] after it.
 * Env/fish callers have no local action row to save (they patch MonitorEnv/MonitorFish beforehand), so ordering
 * is moot and they use the combined [execute].
 *
 * The recompute is always triggered; [RecomputeMissionValidation] runs a forced full compute and
 * [fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.SyncMissionValidation] persists the mission row
 * only when its value actually changed, so no per-action change-guard is needed here.
 */
@UseCase
class ComputeActionValidityAndRecomputeMission(
    private val getMissionDates: GetMissionDates,
    private val entityValidityValidator: EntityValidityValidator,
    private val recomputeMissionValidation: RecomputeMissionValidation,
) {
    /**
     * Env/fish combined step (no local row save to order around).
     *
     * @param action already patched, with targets/sati resolved.
     * @param ownerId the nav mission owner (used to key the nav mission + resolve its dates); null for env/fish.
     * @param inquiryId set only for nav INQUIRY actions.
     */
    fun execute(action: MissionActionEntity, ownerId: UUID?, inquiryId: UUID? = null) {
        computeActionValidity(action = action, ownerId = ownerId, inquiryId = inquiryId)
        recomputeMission(action = action, ownerId = ownerId)
    }

    /** Computes the action's completeness for stats. Nav callers must run this BEFORE saving the nav row. */
    fun computeActionValidity(action: MissionActionEntity, ownerId: UUID?, inquiryId: UUID? = null) {
        val missionDates = getMissionDates.execute(missionId = action.missionId, ownerId = ownerId, inquiryId = inquiryId)
        val policy = ValidationPolicies.forMissionStartDate(missionDates?.startDateTimeUtc)
        action.computeValidity(validator = entityValidityValidator, policy = policy)
    }

    /** Recomputes the parent mission's validation. Nav callers must run this AFTER saving the nav row. */
    fun recomputeMission(action: MissionActionEntity, ownerId: UUID?) {
        when (action.source) {
            MissionSourceEnum.RAPPORT_NAV -> recomputeMissionValidation.forNavMission(ownerId)
            else -> recomputeMissionValidation.forEnvMission(action.missionId)
        }
    }
}
