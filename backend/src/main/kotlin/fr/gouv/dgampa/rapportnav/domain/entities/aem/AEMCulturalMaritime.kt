package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMCulturalMaritime(
    val nbrOfHourInSea: Int = 0, //4.4.1
    val nbrOfScientificOperation: Int = 0, // 4.4.2
    val nbrOfBCMPoliceOperation: Int = 0, // 4.4.3
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfScientificOperation = scientificCampaignActionEntities(envActions).size,
        nbrOfBCMPoliceOperation = culturalMaritimeActionEntities(envActions).size,
        nbrOfHourInSea = AEMUtils.getEnvDurationInHours(culturalMaritimeActionEntities(envActions)).toInt(),
    )

    companion object {
        private fun culturalMaritimeActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            val culturalMaritime = listOf(104);
            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.map { c -> c.themeId }
                    ?.containsAll(culturalMaritime) == true ||
                    action?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.containsAll(culturalMaritime) == true
            }
        }

        private fun scientificCampaignActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {
            val scientificCampaign = listOf(165);
            return envActions.filter { action ->
                action?.controlAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                    ?.containsAll(scientificCampaign) == true ||
                    action?.surveillanceAction?.action?.controlPlans?.flatMap { it.subThemeIds!! }
                        ?.containsAll(scientificCampaign) == true
            }
        }
    }
}

