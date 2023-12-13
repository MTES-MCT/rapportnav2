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
    /**
     * Some controls are marked as to be completed by the centers CNSP/CACEM
     * The Units then have to fill these up in RapportNav
     * This function returns a list of these control types
     */
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

    /**
     * Return a union of what control types should be reported and have been reported
     */
    @SchemaMapping(typeName = "EnvActionData", field = "availableControlTypesForInfraction")
    fun getAvailableControlTypesForInfraction(action: EnvActionData): List<ControlType>? {
        return listOf(
            ControlType.ADMINISTRATIVE.takeIf {
                action.controlAdministrative != null || action.isAdministrativeControl == true
            },
            ControlType.NAVIGATION.takeIf {
                action.controlNavigation != null || action.isComplianceWithWaterRegulationsControl == true
            },
            ControlType.SECURITY.takeIf {
                action.controlSecurity != null || action.isSafetyEquipmentAndStandardsComplianceControl == true
            },
            ControlType.GENS_DE_MER.takeIf {
                action.controlGensDeMer != null || action.isSeafarersControl == true
            }
        ).mapNotNull { it }
    }

    /**
     * Infraction on Env controls are different as they're
     * - by group and not by unit (logique par lot en francais)
     * - grouped by target instead of a flat list
     */
    @SchemaMapping(typeName = "EnvActionData", field = "infractions")
    fun getInfractions(action: EnvActionData): List<InfractionsByVessel> {
        // get infractions coming from different sources
        val envInfractions = action.infractions?.map { Infraction.fromEnvInfractionEntity(it) }.orEmpty()
        val navInfractions = getInfractionsForActionControlEnv.execute(actionId = action.id.toString())
            .map { Infraction.fromInfractionEntity(it) }

        // group them together by vessel
        val unsortedInfractions = envInfractions + navInfractions
        var infractionsByVessel =
            InfractionsByVessel(infractions = unsortedInfractions).groupInfractionsByVesselIdentifier(
                unsortedInfractions
            )

        // add derived information
        infractionsByVessel = infractionsByVessel.map { byVessel ->
            // a target reported by monitorEnv will have an infraction with controlType null
            // the next field is to help the frontend hide/show information according to whom has reported the target
            val targetAddedInRapportNav = !byVessel.infractions.any { it.controlType == null }

            // the next field is to help the frontend show different options for which control type to report an infraction
            val reportedControlTypes = byVessel.infractions.mapNotNull { it.controlType }

            val updatedByVessel = byVessel.copy(
                controlTypesWithInfraction = reportedControlTypes,
                targetAddedInRapportNav = targetAddedInRapportNav
            )
            updatedByVessel
        }


        return infractionsByVessel
    }


}
