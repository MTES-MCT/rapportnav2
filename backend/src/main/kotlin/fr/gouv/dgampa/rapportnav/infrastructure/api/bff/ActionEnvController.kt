package fr.gouv.dgampa.rapportnav.infrastructure.api.bff

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.PatchEnvAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control.GetControlByActionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.infraction.GetInfractionsByActionId
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action.ActionEnvInput
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.EnvActionData
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.FormattedEnvActionControlPlan
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action.PatchedEnvAction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.Infraction
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction.InfractionsByVessel
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.stereotype.Controller


@Controller
class ActionEnvController(
    private val getControlByActionId: GetControlByActionId,
    private val getInfractionsByActionId: GetInfractionsByActionId,
    private val mapEnvActionControlPlans: MapEnvActionControlPlans,
    private val patchEnvAction: PatchEnvAction,
) {

    /**
     * Send a request to MonitorEnv to patch some data on an action
     */
    @MutationMapping
    fun patchActionEnv(@Argument action: ActionEnvInput): PatchedEnvAction? {
        val patchedAction = patchEnvAction.execute(input = action)
        return patchedAction?.let {
            PatchedEnvAction.fromPatchableEnvActionEntity(patchedAction)
        }
    }


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
        val navInfractions = getInfractionsByActionId.execute(actionId = action.id.toString())
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
            val targetAddedByUnit = !byVessel.infractions.any { it.controlType == null }

            // the next field is to help the frontend show different options for which control type to report an infraction
            val reportedControlTypes = byVessel.infractions.mapNotNull { it.controlType }

            val updatedByVessel = byVessel.copy(
                controlTypesWithInfraction = reportedControlTypes,
                targetAddedByUnit = targetAddedByUnit
            )
            updatedByVessel
        }


        return infractionsByVessel
    }

    @SchemaMapping(typeName = "EnvActionData", field = "formattedControlPlans")
    fun getFormattedControlTypes(action: EnvActionData): List<FormattedEnvActionControlPlan>? {
        val filteredControlPlans: ControlPlansEntity? = mapEnvActionControlPlans.execute(action.controlPlans)
        return FormattedEnvActionControlPlan.fromControlPlansEntity(filteredControlPlans)
    }


}
