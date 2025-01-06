package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.BaseControlEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.v2.*

@UseCase
class ProcessMissionActionControl(
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
    private val controlAdministrativeRepo: IControlAdministrativeRepository
) {

    fun execute(actionId: String, controls: ActionControlInput?): ActionControlInput {
        val controlSecurity = processControlSecurity(actionId = actionId, control = controls?.controlSecurity)
        val controlGensDeMer = processControlGensDeMer(actionId = actionId, control = controls?.controlGensDeMer)
        val controlNavigation = processControlNavigation(actionId = actionId, control = controls?.controlNavigation)
        val controlAdministrative =
            processControlAdministrative(actionId = actionId, control = controls?.controlAdministrative)

        return ActionControlInput(
            controlSecurity = controlSecurity,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlAdministrative = controlAdministrative
        )
    }

    private inline fun <M : BaseControlInput, T : BaseControlEntity> processControl(
        actionId: String,
        control: M?,
        findControlByActionId: (String) -> T?,
        saveControl: (T) -> T
    ): T? {
        if (control?.id != null) {
            val existingControl = findControlByActionId(actionId)
            if (control.toEntity().equals(existingControl)) return existingControl
        }
        if (control == null) return null
        System.out.println("[processMissionAction] SAVE: $actionId")
        return saveControl(control.toEntity() as T)
    }


    private fun processControlSecurity(actionId: String, control: ControlSecurityInput2?): ControlSecurityInput2? {
        val response = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlSecurityRepo.save(it).toControlSecurityEntity() },
            findControlByActionId = { controlSecurityRepo.findByActionControlId(it).toControlSecurityEntity() },
        )
        if (response?.id != null) control?.id = response.id
        return control
    }

    private fun processControlGensDeMer(actionId: String, control: ControlGensDeMerInput2?): ControlGensDeMerInput2? {
        val response = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlGensDeMerRepo.save(it).toControlGensDeMerEntity() },
            findControlByActionId = { controlGensDeMerRepo.findByActionControlId(it).toControlGensDeMerEntity() },
        )
        if (response?.id != null) control?.id = response.id
        return control
    }

    private fun processControlNavigation(
        actionId: String,
        control: ControlNavigationInput2?
    ): ControlNavigationInput2? {
        val response = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlNavigationRepo.save(it).toControlNavigationEntity() },
            findControlByActionId = { controlNavigationRepo.findByActionControlId(it).toControlNavigationEntity() },
        )
        if (response?.id != null) control?.id = response.id
        return control
    }

    private fun processControlAdministrative(
        actionId: String,
        control: ControlAdministrativeInput2?
    ): ControlAdministrativeInput2? {
        val response = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlAdministrativeRepo.save(it).toControlAdministrativeEntity() },
            findControlByActionId = {
                controlAdministrativeRepo.findByActionControlId(it).toControlAdministrativeEntity()
            },
        )
        if (response?.id != null) control?.id = response.id
        return control
    }

}
