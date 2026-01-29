package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavMissionActionRepository
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import java.util.*

@UseCase
class GetNavActionById(
    private val processNavAction: ProcessNavAction,
    private val missionActionRepository: INavMissionActionRepository,
) {
    fun execute(actionId: String?): MissionNavActionEntity? {
        if (!isValidUUID(actionId)) return null
        return try {
            val model = missionActionRepository.findById(UUID.fromString(actionId)).orElse(null) ?: return null
            processNavAction.execute(action = MissionNavActionEntity.fromMissionActionModel(model))
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetNavActionById failed for actionId=$actionId",
                originalException = e
            )
        }
    }
}
