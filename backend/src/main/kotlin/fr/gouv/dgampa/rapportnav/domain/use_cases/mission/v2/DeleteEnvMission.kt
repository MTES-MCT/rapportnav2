package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.v2.mission.IEnvMissionRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetEnvMissionById2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetServiceByControlUnit

@UseCase
class DeleteEnvMission(
    private val missionRepo: IEnvMissionRepository,
    private val getEnvMissionById2: GetEnvMissionById2,
    private val getServiceByControlUnit: GetServiceByControlUnit
) {
    fun execute(id: Int?, serviceId: Int?) {
        if (id == null) return
        val mission = getEnvMissionById2.execute(missionId = id)
        val serviceIds = getServiceByControlUnit.execute(controlUnits = mission?.controlUnits).map { it.id }

        if (!serviceIds.contains(serviceId)) return
        if (mission?.envActions?.isNotEmpty() == true) return
        return missionRepo.deleteMission(missionId = id)
    }
}
