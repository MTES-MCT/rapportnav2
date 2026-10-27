package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitResourceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillReport
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillSkip
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamBackfillSkipReason
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamMetricsMergeResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IResourceUsageRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionByExternalId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.controlUnitResource.GetPamShipResource
import org.slf4j.LoggerFactory
import java.util.UUID

/**
 * Copies the PAM metrics of mission_general_info onto the PAM ship's row of mission_resource_usage
 * (Phase 1 backfill). Only the three PAM usage columns are written; mission_general_info is never modified.
 * Idempotent: a rerun reports the already-migrated rows as unchanged. [execute] defaults to a dry run.
 */
@UseCase
class BackfillPamResourceUsage(
    private val generalInfoRepository: IMissionGeneralInfoRepository,
    private val resourceUsageRepository: IResourceUsageRepository,
    private val getPamShipResource: GetPamShipResource,
    private val getMissionByExternalId: GetMissionByExternalId,
) {
    private val logger = LoggerFactory.getLogger(BackfillPamResourceUsage::class.java)

    fun execute(dryRun: Boolean = true, pageSize: Int = DEFAULT_PAGE_SIZE): PamBackfillReport {
        val run = Run(dryRun = dryRun)

        var page = 0
        do {
            val result = generalInfoRepository.findWithPamMetricsPaginated(page = page, size = pageSize)
            result.content.forEach { run.process(MissionGeneralInfoEntity.fromMissionGeneralInfoModel(it)) }
            page++
        } while (result.hasNext())

        return run.report().also {
            logger.info(
                "BackfillPamResourceUsage: dryRun=${it.dryRun} scanned=${it.scanned} created=${it.created} " +
                    "updated=${it.updated} unchanged=${it.unchanged} skipped=${it.skipped.size}"
            )
        }
    }

    private inner class Run(val dryRun: Boolean) {
        // Ship per service id, resolved once: one MonitorEnv call per distinct PAM control unit (null = unresolved,
        // already logged). resolve() lets a MonitorEnv failure abort the run rather than report every row as NO_SHIP.
        private val shipByServiceId = mutableMapOf<Int?, LegacyControlUnitResourceEntity?>()
        private val migratedMissions = mutableSetOf<UUID>()
        private val outcomes = mutableMapOf<PamMetricsMergeResult, Int>()
        private val skipped = mutableListOf<PamBackfillSkip>()
        private var scanned = 0

        fun process(generalInfo: MissionGeneralInfoEntity) {
            scanned++
            val service = generalInfo.service
                ?: return skip(generalInfo, null, PamBackfillSkipReason.NO_SERVICE)
            val ship = shipByServiceId.getOrPutNullable(service.id) { getPamShipResource.resolve(service) }
                ?: return skip(generalInfo, null, PamBackfillSkipReason.NO_SHIP, "service id=${service.id}")
            val missionIdUUID = generalInfo.missionIdUUID
                ?: generalInfo.missionId?.let { getMissionByExternalId.execute(it.toString())?.id }
                ?: return skip(generalInfo, null, PamBackfillSkipReason.NO_MISSION_UUID)
            if (!migratedMissions.add(missionIdUUID)) {
                return skip(generalInfo, missionIdUUID, PamBackfillSkipReason.DUPLICATE_MISSION)
            }

            val metrics = PamResourceMetrics.fromMissionGeneralInfoEntity(generalInfo)
            val outcome = try {
                if (dryRun) {
                    val existing = resourceUsageRepository.findByMissionId(missionIdUUID).firstOrNull { it.resourceId == ship.id }
                    metrics.mergeOutcome(existing)
                } else {
                    resourceUsageRepository.mergePamMetrics(missionIdUUID = missionIdUUID, resourceId = ship.id, metrics = metrics)
                }
            } catch (e: Exception) {
                return skip(generalInfo, missionIdUUID, PamBackfillSkipReason.WRITE_FAILED, e.message)
            }
            outcomes[outcome] = (outcomes[outcome] ?: 0) + 1
        }

        fun report() = PamBackfillReport(
            dryRun = dryRun,
            scanned = scanned,
            created = outcomes[PamMetricsMergeResult.CREATED] ?: 0,
            updated = outcomes[PamMetricsMergeResult.UPDATED] ?: 0,
            unchanged = outcomes[PamMetricsMergeResult.UNCHANGED] ?: 0,
            skipped = skipped.toList()
        )

        private fun skip(
            generalInfo: MissionGeneralInfoEntity,
            missionIdUUID: UUID?,
            reason: PamBackfillSkipReason,
            detail: String? = null
        ) {
            skipped += PamBackfillSkip(
                generalInfoId = generalInfo.id,
                missionId = generalInfo.missionId,
                missionIdUUID = missionIdUUID ?: generalInfo.missionIdUUID,
                reason = reason,
                detail = detail
            )
        }
    }

    // Kotlin's getOrPut recomputes when the stored value is null; an unresolved ship must be cached too.
    private fun <K, V> MutableMap<K, V?>.getOrPutNullable(key: K, compute: () -> V?): V? =
        if (containsKey(key)) get(key) else compute().also { put(key, it) }

    companion object {
        const val DEFAULT_PAGE_SIZE = 500
    }
}
