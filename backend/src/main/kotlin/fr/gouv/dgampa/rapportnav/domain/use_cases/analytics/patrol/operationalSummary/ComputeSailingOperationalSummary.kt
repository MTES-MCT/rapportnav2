package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity

@UseCase
class ComputeSailingOperationalSummary(
) {

    fun getProSailingSeaSummary(actions: List<ActionEntity>): Map<String, Int> {
        val summary = getNavActionSummary(
            actions = actions,
            controlMethod = ControlMethod.SEA,
            vesselType = VesselTypeEnum.SAILING
        )
        val keysToKeep = setOf("nbActions", "nbPvSecuAndAdmin", "nbPVGensDeMer", "nbPvNav")
        return summary.filterKeys { it in keysToKeep }
    }

    fun getProSailingLandSummary(actions: List<ActionEntity>): Map<String, Int> {
        val summary = getNavActionSummary(
            actions = actions,
            controlMethod = ControlMethod.LAND,
            vesselType = VesselTypeEnum.SAILING
        )
        val keysToKeep = setOf("nbActions", "nbPvSecuAndAdmin", "nbPVGensDeMer")
        val filteredMap = summary.filterKeys { it in keysToKeep }
        return filteredMap
    }

    fun getLeisureSailingSeaSummary(actions: List<ActionEntity>): Map<String, Int> {
        val summary = getNavActionSummary(
            actions = actions,
            controlMethod = ControlMethod.SEA,
            vesselType = VesselTypeEnum.SAILING_LEISURE
        )
        val keysToKeep = setOf("nbActions", "nbPvSecu", "nbPVAdmin", "nbPvNav")
        return summary.filterKeys { it in keysToKeep }
    }

    fun getLeisureSailingLandSummary(actions: List<ActionEntity>): Map<String, Int> {
        val summary = getNavActionSummary(
            actions = actions,
            controlMethod = ControlMethod.LAND,
            vesselType = VesselTypeEnum.SAILING_LEISURE
        )
        val keysToKeep = setOf("nbActions", "nbPvSecu", "nbPVAdmin")
        return summary.filterKeys { it in keysToKeep }
    }

    private fun getNavActionSummary(
        actions: List<ActionEntity>,
        vesselType: VesselTypeEnum,
        controlMethod: ControlMethod
    ): Map<String, Int> {
        val filteredActions = actions
            .filterIsInstance<NavActionEntity>()
            .filter { it.actionType == ActionType.CONTROL }
            .filter { it.vesselType == vesselType && it.controlMethod == controlMethod }

        return getNavSummary(filteredActions)
    }

    private fun getNavSummary(actions: List<NavActionEntity>): Map<String, Int> {
        return mapOf(
            // Nbre Navires contrôlés
            "nbActions" to actions.size,
            // Nbre PV équipmt sécu. permis nav.
            // Nb PV dans "équipement sécu" + "doc administratif navire" ajouté par unité dans Rap Nav
            "nbPvSecuAndAdmin" to actions.sumOf { action ->
                action.getInfractionByControlType(controlType = ControlType.SECURITY).count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                } +
                    action.getInfractionByControlType(controlType = ControlType.ADMINISTRATIVE).count { infraction ->
                        infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                    }
            },
            // Nb PV dans "Administrative" ajouté par unité dans Rap Nav
            "nbPVAdmin" to actions.sumOf { action ->
                action.getInfractionByControlType(controlType = ControlType.ADMINISTRATIVE).count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                }
            },
            // Nb PV dans "Security" ajouté par unité dans Rap Nav
            "nbPvSecu" to actions.sumOf { action ->
                action.getInfractionByControlType(controlType = ControlType.SECURITY).count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                }
            },
            // Nbre PV titre navig. rôle/déc. eff
            // Nb PV dans "Gens de mer" ajouté par unité dans Rap Nav
            "nbPVGensDeMer" to actions.sumOf { action ->
                action.getInfractionByControlType(controlType = ControlType.GENS_DE_MER).count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                }
            },
            // Nbre PV police navig.
            // Nb PV dans "Police de la navigation" ajouté par unité dans Rap Nav
            "nbPvNav" to actions.sumOf { action ->
                action.getInfractionByControlType(controlType = ControlType.NAVIGATION).count { infraction ->
                    infraction.infractionType == InfractionTypeEnum.WITH_REPORT
                }
            },
        )
    }

}
