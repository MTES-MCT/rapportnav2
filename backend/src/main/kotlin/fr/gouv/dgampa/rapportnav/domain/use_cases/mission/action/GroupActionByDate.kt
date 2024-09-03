package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import java.time.LocalDate
import java.time.ZoneId

@UseCase
class GroupActionByDate {
    fun execute(actions: List<MissionActionEntity>?): Map<LocalDate, List<MissionActionEntity>>? {
        val zoneId = ZoneId.of("Europe/Paris") // Or use a specific ZoneId like ZoneId.of("Europe/Paris")

        return actions?.groupBy { action ->
            val instant = when (action) {
                is MissionActionEntity.EnvAction -> {
                    val controlActionDate =
                        action.envAction?.controlAction?.action?.actionStartDateTimeUtc
                    val surveillanceActionDate =
                        action.envAction?.surveillanceAction?.action?.actionStartDateTimeUtc
                    controlActionDate ?: surveillanceActionDate
                }

                is MissionActionEntity.FishAction -> action.fishAction.controlAction?.action?.actionDatetimeUtc
                is MissionActionEntity.NavAction -> action.navAction.startDateTimeUtc
            }
            instant?.atZone(zoneId)?.toLocalDate()
        }?.filterKeys { it != null }
            ?.mapKeys { (key, _) -> key!! }
    }
}
