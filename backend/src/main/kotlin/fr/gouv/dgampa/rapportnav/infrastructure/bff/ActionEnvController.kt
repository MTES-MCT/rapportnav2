package fr.gouv.dgampa.rapportnav.infrastructure.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.missions.infraction.GetInfractionsForActionControlEnv
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.EnvActionData
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.InfractionsByVessel
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionEnvController(
    private val getControlByActionId: GetControlByActionId,
    private val getInfractionsForActionControlEnv: GetInfractionsForActionControlEnv
) {
    @SchemaMapping(typeName = "EnvActionData", field = "controlsToComplete")
    fun getControlsToComplete(action: EnvActionData): List<ControlType>? {
        return listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                action.isAdministrativeControl == true &&
                        getControlByActionId.getControlAdministrative(actionControlId = action.id.toString()) == null
            },
            ControlType.NAVIGATION.takeIf {
                action.isComplianceWithWaterRegulationsControl == true &&
                        getControlByActionId.getControlNavigation(actionControlId = action.id.toString()) == null
            },
            ControlType.SECURITY.takeIf {
                action.isSafetyEquipmentAndStandardsComplianceControl == true &&
                        getControlByActionId.getControlSecurity(actionControlId = action.id.toString()) == null
            },
            ControlType.GENS_DE_MER.takeIf {
                action.isSeafarersControl == true &&
                        getControlByActionId.getControlGensDeMer(actionControlId = action.id.toString()) == null
            }
        ).mapNotNull { it }
    }

    @SchemaMapping(typeName = "EnvActionData", field = "availableControlTypes")
    fun getPossibleControlTypes(action: EnvActionData): List<ControlType>? {
        return listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                action.isAdministrativeControl == true
            },
            ControlType.NAVIGATION.takeIf {
                action.isComplianceWithWaterRegulationsControl == true
            },
            ControlType.SECURITY.takeIf {
                action.isSafetyEquipmentAndStandardsComplianceControl == true
            },
            ControlType.GENS_DE_MER.takeIf {
                action.isSeafarersControl == true
            }
        ).mapNotNull { it }
    }


    @SchemaMapping(typeName = "EnvActionData", field = "infractions")
    fun getInfractions(action: EnvActionData): List<InfractionsByVessel> {
        val envInfractions = action.infractions?.map { Infraction.fromEnvInfractionEntity(it) }.orEmpty()
        val navInfractions = getInfractionsForActionControlEnv.execute(actionId = action.id.toString())
            .map { Infraction.fromInfractionEntity(it) }
        val unsortedInfractions = envInfractions + navInfractions
        val infractionsByVessel =
            InfractionsByVessel(infractions = unsortedInfractions).groupInfractionsByVesselIdentifier(
                unsortedInfractions
            )


        return infractionsByVessel
    }

}
