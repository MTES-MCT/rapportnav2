package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import java.time.LocalDate
import java.time.ZoneId

@UseCase
class GroupActionByDate {
    fun execute(actions: List<MissionActionEntity>?): Map<LocalDate, List<MissionActionEntity>>? {
        val zoneId = ZoneId.of("Europe/Paris") // Or use a specific ZoneId like ZoneId.of("Europe/Paris")

        return actions?.groupBy { action ->
            val instant = action.startDateTimeUtc
            instant?.atZone(zoneId)?.toLocalDate()
        }?.filterKeys { it != null }
            ?.mapKeys { (key, _) -> key!! }
    }
}
