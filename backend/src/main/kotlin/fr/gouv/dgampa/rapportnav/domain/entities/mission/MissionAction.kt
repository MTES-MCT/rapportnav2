package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorEnv.envActions.EnvActionEntity
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable


@Serializable
sealed class MissionAction {
    @Serializable
    data class EnvAction(@Contextual val envAction: EnvActionEntity) : MissionAction()

    @Serializable
    data class FishAction(@Contextual val fishAction: fr.gouv.dgampa.rapportnav.domain.entities.mission.monitorFish.fishActions.MissionAction) : MissionAction()
    @Serializable
    data class RapportNavAction(val code: Int) : MissionAction()
}
