package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction

data class ExtendedFishActionEntity(
    val controlAction: ExtendedFishActionControlEntity? = null,
) {
    companion object {
        fun fromMissionAction(
            action: MissionAction,
        ): ExtendedFishActionEntity =
            ExtendedFishActionEntity(
                controlAction = ExtendedFishActionControlEntity.fromFishMissionAction(action),
            )
    }

}
