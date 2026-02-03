package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import kotlin.collections.orEmpty


// return all controls coming from Fish + Env + Nav
@UseCase
class GetMissionActionControls {
    fun execute(mission: MissionEntity?):  List<MissionActionEntity> {
        val controlsNav = mission?.actions
            ?.filterIsInstance<MissionNavActionEntity>()
            ?.filter {it.actionType === ActionType.CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controlsFish = mission?.actions
            ?.filterIsInstance<MissionFishActionEntity>()
            ?.filter { it.fishActionType === MissionActionType.LAND_CONTROL || it.fishActionType === MissionActionType.SEA_CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        val controlsEnv = mission?.actions
            ?.filterIsInstance<MissionEnvActionEntity>()
            ?.filter { it.envActionType === ActionTypeEnum.CONTROL }
            ?.sortedBy { it.startDateTimeUtc }
            .orEmpty()

        return controlsNav + controlsFish + controlsEnv
    }
}
