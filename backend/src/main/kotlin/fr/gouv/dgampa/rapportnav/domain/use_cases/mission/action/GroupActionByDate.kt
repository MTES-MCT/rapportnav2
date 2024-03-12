package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import java.time.LocalDate

@UseCase
class GroupActionByDate {
    fun execute(actions: List<MissionActionEntity>?): Map<LocalDate, List<MissionActionEntity>>? {
        return actions?.groupBy { action ->
            when (action) {
                is MissionActionEntity.EnvAction -> action.envAction?.controlAction?.action?.actionStartDateTimeUtc?.toLocalDate()
                is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc?.toLocalDate()
                is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc.toLocalDate()
                else -> null // Handle other types of actions, if any
            }
        }?.mapNotNull { (date, actions) ->
            date?.let { it to actions }
        }?.toMap()
    }
}
