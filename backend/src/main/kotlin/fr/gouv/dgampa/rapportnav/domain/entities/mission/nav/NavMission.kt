package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction

data class NavMission(
    val id: Int,
    val actions: List<NavAction>,
)
