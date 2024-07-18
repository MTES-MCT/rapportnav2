package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMCulturalMaritime(
    val nbrOfHourAtSea: Int = 0, //4.4.1
    val nbrOfScientificOperation: Int = 0, // 4.4.2
    val nbrOfBCMPoliceOperation: Int = 0, // 4.4.3
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfScientificOperation = scientificCampaignActionEntities(envActions).size,
        nbrOfBCMPoliceOperation = culturalMaritimeActionEntities(envActions).size,
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(culturalMaritimeActionEntities(envActions)).toInt(),
    )

    companion object {
        val scientificCampaignControlPlanSubThemeIds = listOf(165);
        val culturalMaritimeWellBeingControlPlanThemeIds = listOf(104);
        private fun culturalMaritimeActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {

            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.map { c -> c.themeId }
                    ?.containsAll(culturalMaritimeWellBeingControlPlanThemeIds) == true ||
                    action?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.containsAll(culturalMaritimeWellBeingControlPlanThemeIds) == true
            }
        }

        private fun scientificCampaignActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {

            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                    ?.containsAll(scientificCampaignControlPlanSubThemeIds) == true ||
                    action?.surveillanceAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                        ?.containsAll(scientificCampaignControlPlanSubThemeIds) == true
            }
        }
    }
}

