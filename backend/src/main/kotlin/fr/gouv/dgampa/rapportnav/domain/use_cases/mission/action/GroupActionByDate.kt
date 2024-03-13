package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity

@UseCase
class GroupActionByDate {
    fun execute(actions: List<MissionActionEntity>?): Map<String, List<MissionActionEntity>>? {
        return actions?.groupBy { action ->
            when (action) {
                is MissionActionEntity.EnvAction -> action.envAction?.controlAction?.action?.actionStartDateTimeUtc?.takeIf { true }
                    ?.toLocalDate().toString()

                is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc?.takeIf { true }
                    ?.toLocalDate().toString()

                is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc.takeIf { true }
                    ?.toLocalDate().toString()

                else -> null // Handle other types of actions, if any
            }
        }?.mapNotNull { (date, actions) ->
            date?.let { it to actions }
        }?.toMap()
    }
}
