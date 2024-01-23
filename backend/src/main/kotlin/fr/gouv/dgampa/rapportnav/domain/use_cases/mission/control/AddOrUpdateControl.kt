package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMerEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class AddOrUpdateControl(
    private val controlAdministrativeRepo: IControlAdministrativeRepository,
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
    private val getControlByActionId: GetControlByActionId,
) {


    fun addOrUpdateControlAdministrative(control: ControlAdministrativeEntity): ControlAdministrativeEntity {
        val existingControl = getControlByActionId.getControlAdministrative(control.actionControlId)
        var controlToSave: ControlAdministrativeEntity? = null
        if (existingControl != null) {
            controlToSave = control
            controlToSave.id = existingControl.id
        } else {
            controlToSave = control
        }
        val savedData = controlAdministrativeRepo.save(controlToSave).toControlAdministrativeEntity()
        return savedData
    }

    fun addOrUpdateControlSecurity(control: ControlSecurityEntity): ControlSecurityEntity {
        val existingControl = getControlByActionId.getControlSecurity(control.actionControlId)
        var controlToSave: ControlSecurityEntity? = null
        if (existingControl != null) {
            controlToSave = control
            controlToSave.id = existingControl.id
        } else {
            controlToSave = control
        }
        val savedData = controlSecurityRepo.save(controlToSave).toControlSecurityEntity()
        return savedData
    }

    fun addOrUpdateControlNavigation(control: ControlNavigationEntity): ControlNavigationEntity {
        val existingControl = getControlByActionId.getControlNavigation(control.actionControlId)
        var controlToSave: ControlNavigationEntity? = null
        if (existingControl != null) {
            controlToSave = control
            controlToSave.id = existingControl.id
        } else {
            controlToSave = control
        }
        val savedData = controlNavigationRepo.save(controlToSave).toControlNavigationEntity()
        return savedData
    }

    fun addOrUpdateControlGensDeMer(control: ControlGensDeMerEntity): ControlGensDeMerEntity {
        val existingControl = getControlByActionId.getControlGensDeMer(control.actionControlId)
        var controlToSave: ControlGensDeMerEntity? = null
        if (existingControl != null) {
            controlToSave = control
            controlToSave.id = existingControl.id
        } else {
            controlToSave = control
        }
        val savedData = controlGensDeMerRepo.save(controlToSave).toControlGensDeMerEntity()
        return savedData
    }
}
