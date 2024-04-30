package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import java.time.LocalDate

@UseCase
class GroupActionByDate {
    fun execute(actions: List<MissionActionEntity>?): Map<LocalDate, List<MissionActionEntity>>? {
        return actions?.groupBy { action ->
            when (action) {
                is MissionActionEntity.EnvAction -> {
                    val controlActionDate =
                        action.envAction?.controlAction?.action?.actionStartDateTimeUtc?.toLocalDate()
                    val surveillanceActionDate =
                        action.envAction?.surveillanceAction?.action?.actionStartDateTimeUtc?.toLocalDate()
                    controlActionDate ?: surveillanceActionDate
                }

                is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc?.toLocalDate()
                is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc.toLocalDate()
            }
        }?.mapNotNull { (date, actions) ->
            date?.let { it to actions }
        }?.toMap()
    }
}
