package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GetStatusForAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetMissionExternalId
import fr.gouv.dgampa.rapportnav.domain.utils.isValidUUID
import java.util.*

@UseCase
class GetActionByOwnerId(
    private val getNavActionById: GetNavActionById,
    private val getEnvActionById: GetEnvActionById,
    private val getFishActionById: GetFishActionById,
    private val getStatusForAction: GetStatusForAction,
    private val getMissionExternalId: GetMissionExternalId,
) {
    fun execute(ownerId: String, actionId: String): MissionActionEntity? {
        // Try nav action first (actionId is a UUID for nav actions)
        val navAction = getNavActionById.execute(actionId = actionId)
        if (navAction != null) {
            navAction.status = getStatusForAction.execute(
                ownerId = navAction.ownerId,
                actionStartDateTimeUtc = navAction.startDateTimeUtc
            )
            return navAction
        }

        // For env/fish actions, resolve the external Int id from the mission table
        val missionUUID = if (isValidUUID(ownerId)) UUID.fromString(ownerId) else null
        val externalId = missionUUID?.let {
            getMissionExternalId.execute(it)
        } ?: ownerId.toIntOrNull()

        if (externalId != null) {
            getFishActionById.execute(missionId = externalId, actionId = actionId)?.let { return it }
            getEnvActionById.execute(missionId = externalId, actionId = actionId)?.let { return it }
        }

        return null
    }
}
