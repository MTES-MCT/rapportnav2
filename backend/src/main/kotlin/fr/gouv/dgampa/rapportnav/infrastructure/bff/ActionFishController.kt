package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.FishActionData
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionFishController(
    private val getControlByActionId: GetControlByActionId,
) {

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlAdministrative")
    fun getControlAdministrative(action: FishActionData): ControlAdministrative? {
        return action.id?.let { id ->
            ControlAdministrative.fromControlAdministrativeEntity(getControlByActionId.getControlAdministrative(actionControlId = id.toString()))
        }
    }
    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlSecurity")
    fun getControlSecurity(action: FishActionData): ControlSecurity? {
        return action.id?.let { id ->
            ControlSecurity.fromControlSecurityEntity(getControlByActionId.getControlSecurity(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlNavigation")
    fun getControlNavigation(action: FishActionData): ControlNavigation? {
        return action.id?.let { id ->
            ControlNavigation.fromControlNavigationEntity(getControlByActionId.getControlNavigation(actionControlId = id.toString()))
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlGensDeMer")
    fun getControlGensDeMer(action: FishActionData): ControlGensDeMer? {
        return action.id?.let { id ->
            ControlGensDeMer.fromControlGensDeMerEntity(getControlByActionId.getControlGensDeMer(actionControlId = id.toString()))
        }
    }

    @SchemaMapping(typeName = "FishActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: FishActionData): List<ControlType>? {
        val controlsToCompleteList = mutableListOf<ControlType>()

        if (action.isAdministrativeControl == true && getControlByActionId.getControlAdministrative(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.ADMINISTRATIVE)
        }

        if (action.isComplianceWithWaterRegulationsControl == true && getControlByActionId.getControlNavigation(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.NAVIGATION)
        }

        if (action.isSafetyEquipmentAndStandardsComplianceControl == true && getControlByActionId.getControlSecurity(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.SECURITY)
        }

        if (action.isSeafarersControl == true && getControlByActionId.getControlGensDeMer(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.GENS_DE_MER)
        }

        return controlsToCompleteList.ifEmpty { null }
    }

}
