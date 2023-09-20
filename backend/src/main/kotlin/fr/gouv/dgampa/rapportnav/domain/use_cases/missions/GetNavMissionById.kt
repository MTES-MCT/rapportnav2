package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMission
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionRepository
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
//    private val controlGensDeMerRepository: IControlGensDeMerRepository,
//    private val controlVesselAdministrativeRepository: IControlVesselAdministrativeRepository,
//    private val controlNavigationRulesRepository: IControlNavigationRulesRepository,
//    private val controlEquipmentAndSecurityRepository: IControlEquipmentAndSecurityRepository,
    private val navActionRepository: INavActionRepository
    ) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int): NavMission {
        val actions = navActionRepository.findAllByMissionId(missionId=missionId)
        val mission = NavMission(id = missionId, actions = actions)
//        val administrativeControls = controlVesselAdministrativeRepository.findAllByMissionId(missionId=missionId)
//        val gensDeMerControls = controlGensDeMerRepository.findAllByMissionId(missionId=missionId)
//        val navigationRulesControls = controlNavigationRulesRepository.findAllByMissionId(missionId=missionId)
//        val equipmentAndSecurityControls = controlEquipmentAndSecurityRepository.findAllByMissionId(missionId=missionId)
        return mission
    }

}
