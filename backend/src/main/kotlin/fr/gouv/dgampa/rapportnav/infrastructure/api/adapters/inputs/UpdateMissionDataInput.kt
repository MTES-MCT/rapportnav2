package fr.gouv.dgampa.rapportnav.infrastructure.api.adapters.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionAction
import java.time.ZonedDateTime

data class UpdateMissionDataInput (
    val id: Int? = null,
    // TODO add crew
    val actions: List<MissionAction> = listOf(),
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
) {
//    fun toMissionEntity(): Mission {
//        return Mission(
//            id = this.id,
//            startDateTimeUtc = this.startDateTimeUtc,
//            endDateTimeUtc = this.endDateTimeUtc,
//            actions = this.actions
//        )
//    }
}
