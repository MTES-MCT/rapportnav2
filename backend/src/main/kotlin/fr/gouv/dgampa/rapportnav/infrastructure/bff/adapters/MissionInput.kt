package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMissionEntity

data class MissionInput(
    val id: Int,
    val startDateTimeUtc: String,
    val endDateTimeUtc: String,
    val actions: List<ActionInput>?
) {
    fun toMission(): NavMissionEntity {
        return NavMissionEntity(
            id = id,
            actions = actions?.map { it.toNavAction(missionId = id) } ?: listOf()
        )
    }
}
