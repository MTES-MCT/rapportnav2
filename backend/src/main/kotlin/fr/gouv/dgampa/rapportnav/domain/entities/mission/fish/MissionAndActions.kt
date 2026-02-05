package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction

data class MissionAndActions(
    val mission: MissionFishEntity,
    val actions: List<MissionAction>,
)


typealias FishMission = MissionAndActions
