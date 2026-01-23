package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IFishActionRepository
import org.springframework.cache.annotation.Cacheable

@UseCase
class GetFishActionListByMissionId(
    private val fishActionRepo: IFishActionRepository
) {
    @Cacheable(value = ["fishActionList"], key = "#missionId")
    fun execute(missionId: Int?): List<MissionAction> {
        if (missionId == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.INVALID_PARAMETERS_EXCEPTION,
                message = "GetFishActionListByMissionId: missionId is required"
            )
        }
        return fishActionRepo.findFishActions(missionId = missionId)
    }
}
