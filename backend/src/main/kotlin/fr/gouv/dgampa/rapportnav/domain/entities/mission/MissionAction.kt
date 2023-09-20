package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
sealed class MissionAction {
    @Serializable
    data class EnvAction(@Contextual val envAction: EnvActionEntity) : MissionAction()

    @Serializable
    data class FishAction(@Contextual val fishAction: fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction) : MissionAction()
    @Serializable
    data class NavAction(@Contextual val navAction: fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavAction) : MissionAction()
}
