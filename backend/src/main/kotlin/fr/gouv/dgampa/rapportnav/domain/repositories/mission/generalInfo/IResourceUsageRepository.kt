package fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.ResourceUsageEntity
import java.util.UUID

interface IResourceUsageRepository {
    fun findByMissionId(missionIdUUID: UUID): List<ResourceUsageEntity>

    /** Reconciles the usage rows for a mission: deletes the current set, then persists [usages]. */
    fun replaceForMission(missionIdUUID: UUID, usages: List<ResourceUsageEntity>)
}
