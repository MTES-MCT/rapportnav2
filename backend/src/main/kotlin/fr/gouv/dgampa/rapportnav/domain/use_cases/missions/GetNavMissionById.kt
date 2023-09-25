package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlEquipmentAndSecurityRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlNavigationRulesRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.control.IControlVesselAdministrativeRepository
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val controlGensDeMerRepository: IControlGensDeMerRepository,
    private val controlVesselAdministrativeRepository: IControlVesselAdministrativeRepository,
    private val controlNavigationRulesRepository: IControlNavigationRulesRepository,
    private val controlEquipmentAndSecurityRepository: IControlEquipmentAndSecurityRepository,
    ) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int): Any {
        val administrativeControls = controlVesselAdministrativeRepository.findAllByMissionId(missionId=missionId)
        val gensDeMerControls = controlGensDeMerRepository.findAllByMissionId(missionId=missionId)
        val navigationRulesControls = controlNavigationRulesRepository.findAllByMissionId(missionId=missionId)
        val equipmentAndSecurityControls = controlEquipmentAndSecurityRepository.findAllByMissionId(missionId=missionId)
        return administrativeControls
    }

}
