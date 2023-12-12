package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity


data class ExtendedEnvMissionEntity(
    val mission: MissionEntity,
    val actions: List<ExtendedEnvActionEntity?>? = null
) {
    companion object {
        fun fromEnvMission(mission: MissionEntity): ExtendedEnvMissionEntity = ExtendedEnvMissionEntity(
            mission = mission,
            actions = mission.envActions?.map { ExtendedEnvActionEntity.fromEnvActionEntity(it) }
        )
    }
}