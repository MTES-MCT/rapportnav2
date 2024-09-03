package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import java.time.Instant
import java.util.*

interface BaseAction {
    val id: UUID
    val missionId: Int
    var isCompleteForStats: Boolean?
    var sourcesOfMissingDataForStats: List<MissionSourceEnum>?
    val startDateTimeUtc: Instant
    val endDateTimeUtc: Instant?
    val observations: String?
}
