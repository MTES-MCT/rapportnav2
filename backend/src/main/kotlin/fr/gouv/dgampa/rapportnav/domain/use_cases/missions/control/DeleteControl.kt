package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import java.time.ZonedDateTime
import java.util.*

@UseCase
class DeleteControl(
    private val controlRepository: INavActionControlRepository,
    private val controlAdministrativeRepository: IControlAdministrativeRepository,
    private val controlGensDeMerRepository: IControlGensDeMerRepository,
    private val controlSecurityRepository: IControlSecurityRepository,
    private val controlNavigationRepository: IControlNavigationRepository
) {
    fun execute(id: UUID): Boolean {
        if (this.controlRepository.existsById(id)) {

            if (controlGensDeMerRepository.existsByActionControlId(actionControlId = id.toString())) {
                val controlGensDeMer = controlGensDeMerRepository.findByActionControlId(actionControlId = id.toString())
                controlGensDeMer.deletedAt = ZonedDateTime.now()
                controlGensDeMerRepository.save(controlGensDeMer.toControlGensDeMerEntity())

            }
            if (controlAdministrativeRepository.existsByActionControlId(actionControlId = id.toString())) {
                val controlAdministrative = controlAdministrativeRepository.findByActionControlId(actionControlId = id.toString())
                controlAdministrative.deletedAt = ZonedDateTime.now()
                controlAdministrativeRepository.save(controlAdministrative.toControlAdministrativeEntity())
            }
            if (controlNavigationRepository.existsByActionControlId(actionControlId = id.toString())) {
                val controlNavigation = controlNavigationRepository.findByActionControlId(actionControlId = id.toString())
                controlNavigation.deletedAt = ZonedDateTime.now()
                controlNavigationRepository.save(controlNavigation.toControlNavigationEntity())
            }
            if (controlSecurityRepository.existsByActionControlId(actionControlId = id.toString())) {
                val controlSecurity = controlSecurityRepository.findByActionControlId(actionControlId = id.toString())
                controlSecurity.deletedAt = ZonedDateTime.now()
                controlSecurityRepository.save(controlSecurity.toControlSecurityEntity())
            }

            val controlAction = this.controlRepository.findById(id = id)
            controlAction.deletedAt = ZonedDateTime.now()
            this.controlRepository.save(controlAction.toActionControl())
//            this.controlRepository.deleteById(id)
            return true
        }
        return false
    }
}
