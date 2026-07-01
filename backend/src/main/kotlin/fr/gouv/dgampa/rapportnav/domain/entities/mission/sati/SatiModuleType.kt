package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType


enum class SatiModuleType {
    M1,
    M3,
    M5,
    M6,
    M7;

    companion object {
        fun fromMissionActionType(type: MissionActionType): SatiModuleType = when (type) {
            MissionActionType.SEA_CONTROL -> M1
            MissionActionType.LAND_CONTROL -> M3
            MissionActionType.AIR_CONTROL -> M5
            MissionActionType.AIR_SURVEILLANCE -> M6
            MissionActionType.OBSERVATION -> M7
        }
    }
}
