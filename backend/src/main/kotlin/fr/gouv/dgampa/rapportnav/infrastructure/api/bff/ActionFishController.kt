package fr.gouv.dgampa.rapportnav.infrastructure.api.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchFishAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionFishInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FishActionData
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.PatchedFishAction
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionFishController(
    private val getControlByActionId: GetControlByActionId,
    private val patchFishAction: PatchFishAction
) {

    /**
     * Send a request to MonitorFish to patch some data on an Action
     */
    @MutationMapping
    fun patchActionFish(@Argument action: ActionFishInput): PatchedFishAction? {
        val patchedAction = patchFishAction.execute(input = action)
        return patchedAction?.let {
            PatchedFishAction.fromMissionAction(patchedAction)
        }
    }

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
