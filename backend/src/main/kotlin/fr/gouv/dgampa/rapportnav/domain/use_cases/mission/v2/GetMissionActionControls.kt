package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import kotlin.collections.orEmpty


// return all controls coming from Fish + Env + Nav
@UseCase
class GetMissionActionControls {
    fun execute(mission: MissionEntity?):  List<ActionEntity> {
        val controlsNav = mission?.actions
            ?.filterIsInstance<NavActionEntity>()
            ?.filter {it.actionType === ActionType.CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controlsFish = mission?.actions
            ?.filterIsInstance<FishActionEntity>()
            ?.filter { it.fishActionType === MissionActionType.LAND_CONTROL || it.fishActionType === MissionActionType.SEA_CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controlsEnv = mission?.actions
            ?.filterIsInstance<EnvActionEntity>()
            ?.filter { it.envActionType === ActionTypeEnum.CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        return controlsNav + controlsFish + controlsEnv
    }
}
