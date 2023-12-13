package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class GetControlByActionId(
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
) {

    // TODO not return Any
    fun getAnyControl(actionControlId: String): Any? {
        return listOf(
            getControlAdministrative(actionControlId),
            getControlSecurity(actionControlId),
            getControlNavigation(actionControlId),
            getControlGensDeMer(actionControlId)
        ).firstOrNull { it != null }
    }

    fun getControlForControlType(actionControlId: String, controlType: ControlType?): Any? {
        return when (controlType) {
            ControlType.ADMINISTRATIVE -> getControlAdministrative(actionControlId)
            ControlType.GENS_DE_MER -> getControlGensDeMer(actionControlId)
            ControlType.NAVIGATION -> getControlNavigation(actionControlId)
            ControlType.SECURITY -> getControlSecurity(actionControlId)
            else -> null
        }
    }


    fun getControlAdministrative(actionControlId: String): ControlAdministrativeEntity? {
        if (controlAdministrativeRepo.existsByActionControlId(actionControlId = actionControlId)) {
            val controlModel = controlAdministrativeRepo.findByActionControlId(actionControlId = actionControlId)
            return controlModel.toControlAdministrativeEntity()
        }
        return null
    }

    fun getControlSecurity(actionControlId: String): ControlSecurityEntity? {
        if (controlSecurityRepo.existsByActionControlId(actionControlId = actionControlId)) {
            val controlModel = controlSecurityRepo.findByActionControlId(actionControlId = actionControlId)
            return controlModel.toControlSecurityEntity()
        }
        return null
    }

    fun getControlNavigation(actionControlId: String): ControlNavigationEntity? {
        if (controlNavigationRepo.existsByActionControlId(actionControlId = actionControlId)) {
            val controlModel = controlNavigationRepo.findByActionControlId(actionControlId = actionControlId)
            return controlModel.toControlNavigationEntity()
        }
        return null
    }

    fun getControlGensDeMer(actionControlId: String): ControlGensDeMerEntity? {
        if (controlGensDeMerRepo.existsByActionControlId(actionControlId = actionControlId)) {
            val controlModel = controlGensDeMerRepo.findByActionControlId(actionControlId = actionControlId)
            return controlModel.toControlGensDeMerEntity()
        }
        return null
    }
}
