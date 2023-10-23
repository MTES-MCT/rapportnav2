package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
sealed class MissionActionEntity {
    @Serializable
    data class EnvAction(@Contextual val envAction: EnvActionEntity) : MissionActionEntity()
    @Serializable
    data class FishAction(@Contextual val fishAction: MissionAction) : MissionActionEntity()
    @Serializable
    data class NavAction(@Contextual val navAction: NavActionEntity) : MissionActionEntity()
}
