package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.v2.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlNavigationRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository
import java.util.*

@UseCase
class ProcessMissionActionControl(
    private val controlSecurityRepo: IControlSecurityRepository,
    private val controlNavigationRepo: IControlNavigationRepository,
    private val controlGensDeMerRepo: IControlGensDeMerRepository,
    private val controlAdministrativeRepo: IControlAdministrativeRepository
) {

    fun execute(action: MissionActionEntity): ActionControlEntity {
        val controlSecurity = processControlSecurity(action.getActionId(), control = action.controlSecurity)
        val controlGensDeMer = processControlGensDeMer(action.getActionId(), control = action.controlGensDeMer)
        val controlNavigation = processControlNavigation(action.getActionId(), control = action.controlNavigation)
        val controlAdministrative =
            processControlAdministrative(action.getActionId(), control = action.controlAdministrative)
        return ActionControlEntity(
            controlSecurity = controlSecurity,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlAdministrative = controlAdministrative
        )
    }

    private inline fun <T : BaseControlEntity> processControl(
        actionId: String,
        control: T?,
        findByActionId: (String) -> T?,
        saveControl: (T) -> T
    ): UUID? {
        val existingControl = findByActionId(actionId)
        if (control != null && !control.equals(existingControl)) {
            return saveControl(control).id
        }
        return existingControl?.id
    }


    private fun processControlSecurity(actionId: String, control: ControlSecurityEntity?): ControlSecurityEntity? {
        val id = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlSecurityRepo.save(it).toControlSecurityEntity() },
            findByActionId = { controlSecurityRepo.findByActionControlId(it)?.toControlSecurityEntity() },
        )
        if (id != null && control != null) control.id = id
        return control
    }

    private fun processControlGensDeMer(actionId: String, control: ControlGensDeMerEntity?): ControlGensDeMerEntity? {
        val id = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlGensDeMerRepo.save(it).toControlGensDeMerEntity() },
            findByActionId = { controlGensDeMerRepo.findByActionControlId(it)?.toControlGensDeMerEntity() },
        )
        if (id != null && control != null) control.id = id
        return control
    }

    private fun processControlNavigation(
        actionId: String,
        control: ControlNavigationEntity?
    ): ControlNavigationEntity? {
        val id = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlNavigationRepo.save(it).toControlNavigationEntity() },
            findByActionId = { controlNavigationRepo.findByActionControlId(it)?.toControlNavigationEntity() },
        )
        if (id != null && control != null) control.id = id
        return control
    }

    private fun processControlAdministrative(
        actionId: String,
        control: ControlAdministrativeEntity?
    ): ControlAdministrativeEntity? {
        val id = processControl(
            actionId = actionId,
            control = control,
            saveControl = { controlAdministrativeRepo.save(it).toControlAdministrativeEntity() },
            findByActionId = { controlAdministrativeRepo.findByActionControlId(it).toControlAdministrativeEntity() },
        )
        if (id != null && control != null) control.id = id
        return control
    }
}
