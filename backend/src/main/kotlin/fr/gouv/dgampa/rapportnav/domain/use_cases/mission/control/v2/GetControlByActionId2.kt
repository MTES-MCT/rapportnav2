package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class GetControlByActionId2(
    //TODO: replace all repo by control Repository
    //private val controlRepository: IControlRepository,
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
) {

    fun getAllControl(actionId: String): ActionControlEntity? {
        return ActionControlEntity(
            controlSecurity = getControlSecurity(actionId),
            controlGensDeMer = getControlGensDeMer(actionId),
            controlNavigation = getControlNavigation(actionId),
            controlAdministrative = getControlAdministrative(actionId)
        )
    }

    fun getControlAdministrative(actionId: String): ControlAdministrativeEntity? {
        if (controlAdministrativeRepo.existsByActionControlId(actionId)) {
            val controlModel = controlAdministrativeRepo.findByActionControlId(actionId)
            return controlModel.toControlAdministrativeEntity()
        }
        return null
    }

    fun getControlSecurity(actionId: String): ControlSecurityEntity? {
        if (controlSecurityRepo.existsByActionControlId(actionId)) {
            val controlModel = controlSecurityRepo.findByActionControlId(actionId)
            return controlModel.toControlSecurityEntity()
        }
        return null
    }

    fun getControlNavigation(actionId: String): ControlNavigationEntity? {
        if (controlNavigationRepo.existsByActionControlId(actionId)) {
            val controlModel = controlNavigationRepo.findByActionControlId(actionId)
            return controlModel.toControlNavigationEntity()
        }
        return null
    }

    fun getControlGensDeMer(actionId: String): ControlGensDeMerEntity? {
        if (controlGensDeMerRepo.existsByActionControlId(actionId)) {
            val controlModel = controlGensDeMerRepo.findByActionControlId(actionId)
            return controlModel.toControlGensDeMerEntity()
        }
        return null
    }
}
