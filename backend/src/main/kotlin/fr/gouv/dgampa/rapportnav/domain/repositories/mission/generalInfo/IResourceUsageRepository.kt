package fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamMetricsMergeResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.PamResourceMetrics
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import java.util.UUID

interface IResourceUsageRepository {
    fun findByMissionId(missionIdUUID: UUID): List<ResourceUsageEntity>

    /** Reconciles the usage rows for a mission: deletes the current set, then persists [usages]. */
    fun replaceForMission(missionIdUUID: UUID, usages: List<ResourceUsageEntity>)

    /**
     * Upserts only the PAM columns of the ([missionIdUUID], [resourceId]) row; the ULAM columns are left untouched.
     * No row is created when [metrics] is empty.
     */
    fun mergePamMetrics(missionIdUUID: UUID, resourceId: Int, metrics: PamResourceMetrics): PamMetricsMergeResult
}
