package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMCulturalMaritime(
    val nbrOfHourAtSea: Double? = 0.0, //4.4.1
    val nbrOfScientificOperation: Double? = 0.0, // 4.4.2
    val nbrOfBCMPoliceOperation: Double? = 0.0, // 4.4.3
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfScientificOperation = scientificCampaignActionEntities(envActions).size.toDouble(),
        nbrOfBCMPoliceOperation = culturalMaritimeActionEntities(envActions).size.toDouble(),
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(culturalMaritimeActionEntities(envActions)),
    )

    companion object {
        private val scientificCampaignControlPlanSubThemeIds = listOf(165);
        private val culturalMaritimeWellBeingControlPlanThemeIds = listOf(104);
        private fun culturalMaritimeActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.map { c -> c.themeId }
                    ?.intersect(culturalMaritimeWellBeingControlPlanThemeIds)?.isEmpty() == false ||
                    action?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.intersect(culturalMaritimeWellBeingControlPlanThemeIds)?.isEmpty() == false
            }
        }

        private fun scientificCampaignActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                    ?.intersect(scientificCampaignControlPlanSubThemeIds)?.isEmpty() == false ||
                    action?.surveillanceAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                        ?.intersect(scientificCampaignControlPlanSubThemeIds)?.isEmpty() == false
            }
        }
    }
}

