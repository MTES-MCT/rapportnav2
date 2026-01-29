package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBMissionActionRepository
import java.time.Instant

@UseCase
class GetStatusForAction(
    private val missionActionsRepository: IDBMissionActionRepository,
) {

    fun execute(missionId: Int, actionStartDateTimeUtc: Instant? = null): ActionStatusType {
        val actions = try {
            missionActionsRepository
                .findAllByMissionId(missionId)
                .map { MissionNavActionEntity.fromMissionActionModel(it) }
        } catch (e: BackendInternalException) {
            throw e
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "GetStatusForAction failed for missionId=$missionId",
                originalException = e
            )
        }

        if (actions.isEmpty()) {
            return ActionStatusType.UNKNOWN
        }

        // Filter only STATUS actions with valid dates
        val statusActions = actions
            .filter { it.actionType == ActionType.STATUS && it.startDateTimeUtc != null }

        if (statusActions.isEmpty()) {
            return ActionStatusType.UNKNOWN
        }

        val last = if (actionStartDateTimeUtc == null) {
            // No reference time → return the latest status action overall
            statusActions.maxByOrNull { it.startDateTimeUtc!! }
        } else {
            // Return the last STATUS before or at the given reference time
            statusActions
                .filter { it.startDateTimeUtc!! <= actionStartDateTimeUtc }
                .maxByOrNull { it.startDateTimeUtc!! }
        }

        return last?.status ?: ActionStatusType.UNKNOWN
    }
}
