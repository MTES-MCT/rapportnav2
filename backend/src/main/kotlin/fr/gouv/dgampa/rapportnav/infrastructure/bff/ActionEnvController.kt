package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.EnvActionData
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionEnvController(
    private val getControlByActionId: GetControlByActionId,
) {

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlAdministrative")
    fun getControlAdministrative(action: EnvActionData): ControlAdministrative? {
        return action.id?.let { id ->
            ControlAdministrative.fromControlAdministrativeEntity(getControlByActionId.getControlAdministrative(actionControlId = id.toString()))
        }
    }
    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlSecurity")
    fun getControlSecurity(action: EnvActionData): ControlSecurity? {
        return action.id?.let { id ->
            ControlSecurity.fromControlSecurityEntity(getControlByActionId.getControlSecurity(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlNavigation")
    fun getControlNavigation(action: EnvActionData): ControlNavigation? {
        return action.id?.let { id ->
            ControlNavigation.fromControlNavigationEntity(getControlByActionId.getControlNavigation(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlGensDeMer")
    fun getControlGensDeMer(action: EnvActionData): ControlGensDeMer? {
        return action.id?.let { id ->
            ControlGensDeMer.fromControlGensDeMerEntity(getControlByActionId.getControlGensDeMer(actionControlId = id.toString()))
        }
    }

    @SchemaMapping(typeName = "EnvActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: EnvActionData): List<ControlType>? {
        val controlsToCompleteList = mutableListOf<ControlType>()

        if (action.isAdministrativeControl == true && getControlByActionId.getControlAdministrative(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.ADMINISTRATIVE)
        }

        if (action.isComplianceWithWaterRegulationsControl == true && getControlByActionId.getControlNavigation(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.NAVIGATION)
        }

        if (action.isSafetyEquipmentAndStandardsComplianceControl == true && getControlByActionId.getControlSecurity(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.SECURITY)
        }

        if (action.isSeafarersControl == true && getControlByActionId.getControlGensDeMer(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.GENS_DE_MER)
        }

        return controlsToCompleteList.ifEmpty { null }
    }

}
