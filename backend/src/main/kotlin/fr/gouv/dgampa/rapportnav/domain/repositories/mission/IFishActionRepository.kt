package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction

interface IFishActionRepository {
    fun findFishActions(missionId: Int): List<MissionAction>
}
