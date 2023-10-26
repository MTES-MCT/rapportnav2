package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlAdministrativeByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.EnvActionData
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionEnvController(
    private val getControlAdministrativeByActionId: GetControlAdministrativeByActionId,
) {

//    @MutationMapping
//    fun addOrUpdateControl(@Argument controlAction: ActionControlInput): NavActionControl {
//        val data = controlAction.toActionControl()
//        val aa = addOrUpdateControl.execute(data)
//        return aa.toNavActionControl()
//    }
//
//    @MutationMapping
//    fun deleteControl(@Argument id: UUID): Boolean {
//        val savedData = deleteControl.execute(id)
//        return savedData
//    }

    @SchemaMapping(typeName = "EnvActionData", field = "controlAdministrative")
    fun getControlAdministrative(action: EnvActionData): ControlAdministrative? {
        if (action.id == null) {
            return null
        }
        val cc = getControlAdministrativeByActionId.execute(actionControlId = action.id)?.toControlAdministrative()
        return cc

    }
    @SchemaMapping(typeName = "EnvActionData", field = "amountOfControlsToComplete")
    fun getEnvAmountOfControlsToComplete(action: EnvActionData): Int? {
        return listOf(
            action.isAdministrativeControl,
            action.isComplianceWithWaterRegulationsControl,
            action.isSafetyEquipmentAndStandardsComplianceControl,
            action.isSeafarersControl
        ).count { it == true }
    }

}
