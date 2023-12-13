package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.EnvActionData
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

    @SchemaMapping(typeName = "FishActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: FishActionData): List<ControlType>? {
        val controlsToCompleteList = mutableListOf<ControlType>()

        if (action.isAdministrativeControl == true && getControlByActionId.getControlAdministrative(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.ADMINISTRATIVE)
        }

        if (action.isComplianceWithWaterRegulationsControl == true && getControlByActionId.getControlNavigation(
                actionControlId = action.id!!
            ) == null
        ) {
            controlsToCompleteList.add(ControlType.NAVIGATION)
        }

        if (action.isSafetyEquipmentAndStandardsComplianceControl == true && getControlByActionId.getControlSecurity(
                actionControlId = action.id!!
            ) == null
        ) {
            controlsToCompleteList.add(ControlType.SECURITY)
        }

        if (action.isSeafarersControl == true && getControlByActionId.getControlGensDeMer(actionControlId = action.id!!) == null) {
            controlsToCompleteList.add(ControlType.GENS_DE_MER)
        }

        return controlsToCompleteList.ifEmpty { null }
    }
}
