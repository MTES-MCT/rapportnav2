package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlAdministrativeByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlGensDeMerByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlNavigationByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlSecurityByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.EnvActionData
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionEnvController(
    private val getControlAdministrativeByActionId: GetControlAdministrativeByActionId,
    private val getControlSecurityByActionId: GetControlSecurityByActionId,
    private val getControlNavigationByActionId: GetControlNavigationByActionId,
    private val getControlGensDeMerByActionId: GetControlGensDeMerByActionId,
) {

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlAdministrative")
    fun getControlAdministrative(action: EnvActionData): ControlAdministrative? {
        return action.id?.let { id ->
            ControlAdministrative.fromControlAdministrativeEntity(getControlAdministrativeByActionId.execute(actionControlId = id.toString()))
        }
    }
    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlSecurity")
    fun getControlSecurity(action: EnvActionData): ControlSecurity? {
        return action.id?.let { id ->
            ControlSecurity.fromControlSecurityEntity(getControlSecurityByActionId.execute(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlNavigation")
    fun getControlNavigation(action: EnvActionData): ControlNavigation? {
        return action.id?.let { id ->
            ControlNavigation.fromControlNavigationEntity(getControlNavigationByActionId.execute(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "EnvActionData", field = "controlGensDeMer")
    fun getControlGensDeMer(action: EnvActionData): ControlGensDeMer? {
        return action.id?.let { id ->
            ControlGensDeMer.fromControlGensDeMerEntity(getControlGensDeMerByActionId.execute(actionControlId = id.toString()))
        }
    }

    @SchemaMapping(typeName = "EnvActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: EnvActionData): List<ControlType>? {
        val controlsToCompleteList = mutableListOf<ControlType>()

        if (action.isAdministrativeControl == true && getControlAdministrativeByActionId.execute(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.ADMINISTRATIVE)
        }

        if (action.isComplianceWithWaterRegulationsControl == true && getControlNavigationByActionId.execute(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.NAVIGATION)
        }

        if (action.isSafetyEquipmentAndStandardsComplianceControl == true && getControlSecurityByActionId.execute(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.SECURITY)
        }

        if (action.isSeafarersControl == true && getControlGensDeMerByActionId.execute(actionControlId = action.id.toString()) == null) {
            controlsToCompleteList.add(ControlType.GENS_DE_MER)
        }

        return controlsToCompleteList.ifEmpty { null }
    }

}
