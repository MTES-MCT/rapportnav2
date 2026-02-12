package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMCulturalMaritime(
    val nbrOfHourAtSea: Double? = 0.0, //4.4.1
    val nbrOfScientificOperation: Double? = 0.0, // 4.4.2
    val nbrOfBCMPoliceOperation: Double? = 0.0, // 4.4.3
) {
    constructor(
        envActions: List<MissionEnvActionEntity?>
    ) : this(
        nbrOfScientificOperation = scientificCampaignActionEntities(envActions).size.toDouble(),
        nbrOfBCMPoliceOperation = culturalMaritimeActionEntities(envActions).size.toDouble(),
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(culturalMaritimeActionEntities(envActions)),
    )

    companion object {
        private val scientificCampaignThemeSubThemeIds = listOf(165);
        private val culturalMaritimeWellBeingThemeThemeIds = listOf(104);
        private fun culturalMaritimeActionEntities(envActions: List<MissionEnvActionEntity?>): List<MissionEnvActionEntity?> {
            return envActions.filter { action ->
                action?.themes?.map { t -> t.id }
                    ?.intersect(culturalMaritimeWellBeingThemeThemeIds)?.isEmpty() == false
            }
        }

        private fun scientificCampaignActionEntities(envActions: List<MissionEnvActionEntity?>): List<MissionEnvActionEntity?> {
            return envActions.filter { action ->
                action?.themes?.flatMap { it.subThemes }.orEmpty().map { it.id }.intersect(scientificCampaignThemeSubThemeIds).isNotEmpty()
            }
        }
    }
}

