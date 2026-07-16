package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel

/**
 * Resolves the local mission row from its MonitorEnv/Fish external id.
 *
 * Thin use-case wrapper over [IMissionNavRepository.findByExternalId] so callers depend on a use
 * case rather than the repository directly. Returns null when no local row exists yet.
 */
@UseCase
class GetMissionByExternalId(
    private val missionNavRepository: IMissionNavRepository,
) {
    fun execute(externalId: String): MissionModel? {
        return missionNavRepository.findByExternalId(externalId).orElse(null)
    }
}
