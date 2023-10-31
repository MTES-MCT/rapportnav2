package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlAdministrativeByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlGensDeMerByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlNavigationByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlSecurityByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.FishActionData
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionFishController(
    private val getControlAdministrativeByActionId: GetControlAdministrativeByActionId,
    private val getControlSecurityByActionId: GetControlSecurityByActionId,
    private val getControlNavigationByActionId: GetControlNavigationByActionId,
    private val getControlGensDeMerByActionId: GetControlGensDeMerByActionId,
) {

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlAdministrative")
    fun getControlAdministrative(action: FishActionData): ControlAdministrative? {
        return action.id?.let { id ->
            getControlAdministrativeByActionId.execute(actionControlId = id)?.toControlAdministrative()
        }
    }
    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlSecurity")
    fun getControlSecurity(action: FishActionData): ControlSecurity? {
        return action.id?.let { id ->
            getControlSecurityByActionId.execute(actionControlId = id)?.toControlSecurity()
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlNavigation")
    fun getControlNavigation(action: FishActionData): ControlNavigation? {
        return action.id?.let { id ->
            getControlNavigationByActionId.execute(actionControlId = id)?.toControlNavigation()
        }
    }

    //    TODO decide if this should be here or not
    @SchemaMapping(typeName = "FishActionData", field = "controlGensDeMer")
    fun getControlGensDeMer(action: FishActionData): ControlGensDeMer? {
        return action.id?.let { id ->
            getControlGensDeMerByActionId.execute(actionControlId = id)?.toControlGensDeMer()
        }
    }

    @SchemaMapping(typeName = "FishActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: FishActionData): List<ControlType>? {
        val controlsToCompleteList = mutableListOf<ControlType>()

        if (action.isAdministrativeControl == true && getControlAdministrativeByActionId.execute(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.ADMINISTRATIVE)
        }

        if (action.isComplianceWithWaterRegulationsControl == true && getControlNavigationByActionId.execute(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.NAVIGATION)
        }

        if (action.isSafetyEquipmentAndStandardsComplianceControl == true && getControlSecurityByActionId.execute(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.SECURITY)
        }

        if (action.isSeafarersControl == true && getControlGensDeMerByActionId.execute(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.GENS_DE_MER)
        }

        return controlsToCompleteList.ifEmpty { null }
    }

}
