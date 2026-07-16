package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.util.UUID

/**
 * Resolves a local mission id (UUID) to its MonitorEnv/MonitorFish external id (Int).
 *
 * This is the single canonical place for that translation: MonitorEnv/Fish resources are
 * cached/keyed by the external Int id, while the rest of the app speaks the mission UUID.
 */
@UseCase
class GetMissionExternalId(
    private val missionNavRepository: IMissionNavRepository,
) {
    fun execute(missionId: UUID): Int? {
        return missionNavRepository.findById(missionId).orElse(null)?.externalId?.toIntOrNull()
    }
}
