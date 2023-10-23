package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity

data class NavMissionEntity(
    val id: Int,
    val actions: List<NavActionEntity>,
)
