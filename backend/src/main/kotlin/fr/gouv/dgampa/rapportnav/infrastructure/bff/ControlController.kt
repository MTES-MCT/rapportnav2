package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.AddOrUpdateControlAdministrative
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.AddOrUpdateControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.AddOrUpdateControlNavigation
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.AddOrUpdateControlSecurity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control.ControlAdministrativeInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control.ControlGensDeMerInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control.ControlNavigationInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control.ControlSecurityInput
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.stereotype.Controller


@Controller
class ControlController(
    private val addOrUpdateControlNavigation: AddOrUpdateControlNavigation,
    private val addOrUpdateControlSecurity: AddOrUpdateControlSecurity,
    private val addOrUpdateControlGensDeMer: AddOrUpdateControlGensDeMer,
    private val addOrUpdateControlAdministrative: AddOrUpdateControlAdministrative,
) {

    @MutationMapping
    fun addOrUpdateControlNavigation(@Argument control: ControlNavigationInput): ControlNavigation {
        val data = control.toControlNavigation()
        return addOrUpdateControlNavigation.execute(data).toControlNavigation()
    }

    @MutationMapping
    fun addOrUpdateControlSecurity(@Argument control: ControlSecurityInput): ControlSecurity {
        val data = control.toControlSecurity()
        return addOrUpdateControlSecurity.execute(data).toControlSecurity()
    }

    @MutationMapping
    fun addOrUpdateControlGensDeMer(@Argument control: ControlGensDeMerInput): ControlGensDeMer {
        val data = control.toControlGensDeMerEntity()
        return addOrUpdateControlGensDeMer.execute(data).toControlGensDeMer()
    }

    @MutationMapping
    fun addOrUpdateControlAdministrative(@Argument control: ControlAdministrativeInput): ControlAdministrative {
        val data = control.toControlAdministrativeEntity()
        return addOrUpdateControlAdministrative.execute(data).toControlAdministrative()
    }



}
