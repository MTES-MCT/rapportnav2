package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
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

    private inline fun <T : BaseControlEntity> addOrUpdateControl(
        control: T,
        getExistingControl: (String) -> T?,
        saveControl: (T) -> T
    ): T {
        val existingControl = getExistingControl(control.actionControlId)
        val controlToSave: BaseControlEntity = control
        if (existingControl != null) {
            controlToSave.id = existingControl.id
        }

        if (controlToSave.shouldToggleOnUnitHasConfirmed()) {
            controlToSave.unitHasConfirmed = true
        }

        return saveControl(controlToSave as T)
    }


    fun addOrUpdateControlAdministrative(control: ControlAdministrativeEntity): ControlAdministrativeEntity {
        return addOrUpdateControl(
            control = control,
            getExistingControl = { getControlByActionId.getControlAdministrative(it) },
            saveControl = { controlAdministrativeRepo.save(it).toControlAdministrativeEntity() }
        )
    }

    fun addOrUpdateControlSecurity(control: ControlSecurityEntity): ControlSecurityEntity {
        return addOrUpdateControl(
            control = control,
            getExistingControl = { getControlByActionId.getControlSecurity(it) },
            saveControl = { controlSecurityRepo.save(it).toControlSecurityEntity() }
        )
    }

    fun addOrUpdateControlNavigation(control: ControlNavigationEntity): ControlNavigationEntity {
        return addOrUpdateControl(
            control = control,
            getExistingControl = { getControlByActionId.getControlNavigation(it) },
            saveControl = { controlNavigationRepo.save(it).toControlNavigationEntity() }
        )
    }

    fun addOrUpdateControlGensDeMer(control: ControlGensDeMerEntity): ControlGensDeMerEntity {
        return addOrUpdateControl(
            control = control,
            getExistingControl = { getControlByActionId.getControlGensDeMer(it) },
            saveControl = { controlGensDeMerRepo.save(it).toControlGensDeMerEntity() }
        )
    }
}
