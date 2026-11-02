package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType


enum class SatiModuleType {
    M1,
    M3,
    M4,
    M5,
    M6,
    M7;

    fun hasVessel(): Boolean = this == M1 || this == M3

    fun hasEstablishment(): Boolean = this == M4

    companion object {
        fun fromMissionActionType(type: MissionActionType): SatiModuleType? {
            if (!type.toString().endsWith("_CONTROL")) return null
            return when (type) {
                MissionActionType.SEA_CONTROL -> M1
                MissionActionType.LAND_CONTROL -> M3
                MissionActionType.AIR_CONTROL -> null
                MissionActionType.AIR_SURVEILLANCE -> M6
                MissionActionType.OBSERVATION -> M7
            }
        }

        fun fromActionType(type: ActionType): SatiModuleType? = when (type) {
            ActionType.CONTROL_SECTOR_FISHING -> M4
            ActionType.CONTROL_ROADSIDE -> M5
            else -> null
        }
    }
}
