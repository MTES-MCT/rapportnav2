package fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs.CreateOrUpdateMissionDataInput

interface IEnvMissionRepository {

    fun createMission(input: CreateOrUpdateMissionDataInput): MissionEntity?
}
