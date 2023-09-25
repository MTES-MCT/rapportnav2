package fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions.MissionAction

data class MissionAndActions(
    val mission: Mission,
    val actions: List<MissionAction>,
)


typealias FishMission = MissionAndActions
