package fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionEnv
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.input.PatchMissionInput

interface IEnvMissionRepository {
    fun createMission(mission: MissionEnv): MissionEnvEntity?

    fun update(mission: MissionEnvEntity): MissionEnvEntity?

    fun patchMission(missionId: Int, mission: PatchMissionInput): MissionEnvEntity?
}
