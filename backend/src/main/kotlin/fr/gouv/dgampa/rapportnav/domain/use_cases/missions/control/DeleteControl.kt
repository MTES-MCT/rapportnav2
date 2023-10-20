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

            if (controlGensDeMerRepository.existsByActionControlId(actionControlId = id)) {
                val controlGensDeMer = controlGensDeMerRepository.findByActionControlId(actionControlId = id)
                controlGensDeMer.deletedAt = ZonedDateTime.now()
                controlGensDeMerRepository.save(controlGensDeMer.toControlGensDeMer())

            }
            if (controlAdministrativeRepository.existsByActionControlId(actionControlId = id)) {
                val controlAdministrative = controlAdministrativeRepository.findByActionControlId(actionControlId = id)
                controlAdministrative.deletedAt = ZonedDateTime.now()
                controlAdministrativeRepository.save(controlAdministrative.toControlAdministrative())
            }
            if (controlNavigationRepository.existsByActionControlId(actionControlId = id)) {
                val controlNavigation = controlNavigationRepository.findByActionControlId(actionControlId = id)
                controlNavigation.deletedAt = ZonedDateTime.now()
                controlNavigationRepository.save(controlNavigation.toControlNavigation())
            }
            if (controlSecurityRepository.existsByActionControlId(actionControlId = id)) {
                val controlSecurity = controlSecurityRepository.findByActionControlId(actionControlId = id)
                controlSecurity.deletedAt = ZonedDateTime.now()
                controlSecurityRepository.save(controlSecurity.toControlSecurity())
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
