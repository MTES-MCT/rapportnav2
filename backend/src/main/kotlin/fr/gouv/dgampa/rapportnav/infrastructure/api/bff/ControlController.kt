package fr.gouv.dgampa.rapportnav.infrastructure.api.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.AddOrUpdateControl
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.DeleteControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.control.ControlAdministrativeInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.control.ControlGensDeMerInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.control.ControlNavigationInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.control.ControlSecurityInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller
import java.util.*


@Controller
class ControlController(
    private val addOrUpdateControl: AddOrUpdateControl,
    private val deleteControlByActionId: DeleteControlByActionId,
) {

    @MutationMapping
    fun addOrUpdateControlNavigation(@Argument control: ControlNavigationInput): ControlNavigation {
        val data = control.toControlNavigation()
        return ControlNavigation.fromControlNavigationEntity(addOrUpdateControl.addOrUpdateControlNavigation(data))!!
    }

    @MutationMapping
    fun addOrUpdateControlSecurity(@Argument control: ControlSecurityInput): ControlSecurity {
        val data = control.toControlSecurity()
        return ControlSecurity.fromControlSecurityEntity(addOrUpdateControl.addOrUpdateControlSecurity(data))!!
    }

    @MutationMapping
    fun addOrUpdateControlGensDeMer(@Argument control: ControlGensDeMerInput): ControlGensDeMer {
        val data = control.toControlGensDeMerEntity()
        return ControlGensDeMer.fromControlGensDeMerEntity(addOrUpdateControl.addOrUpdateControlGensDeMer(data))!!
    }

    @MutationMapping
    fun addOrUpdateControlAdministrative(@Argument control: ControlAdministrativeInput): ControlAdministrative {
        val data = control.toControlAdministrativeEntity()
        return ControlAdministrative.fromControlAdministrativeEntity(
            addOrUpdateControl.addOrUpdateControlAdministrative(
                data
            )
        )!!
    }


    @MutationMapping
    fun deleteControlAdministrative(@Argument actionId: UUID): Boolean {
        val savedData = deleteControlByActionId.deleteControlAdministrative(actionId = actionId)
        return savedData
    }

    @MutationMapping
    fun deleteControlSecurity(@Argument actionId: UUID): Boolean {
        val savedData = deleteControlByActionId.deleteControlSecurity(actionId = actionId)
        return savedData
    }

    @MutationMapping
    fun deleteControlNavigation(@Argument actionId: UUID): Boolean {
        val savedData = deleteControlByActionId.deleteControlNavigation(actionId = actionId)
        return savedData
    }

    @MutationMapping
    fun deleteControlGensDeMer(@Argument actionId: UUID): Boolean {
        val savedData = deleteControlByActionId.deleteControlGensDeMer(actionId = actionId)
        return savedData
    }


}
