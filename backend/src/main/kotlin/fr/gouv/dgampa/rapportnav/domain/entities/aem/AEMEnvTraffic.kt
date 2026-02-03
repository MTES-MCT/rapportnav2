package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMEnvTraffic(
    val nbrOfHourAtSea: Double? = 0.0, //3.3.1
    val nbrOfRedirectShip: Double? = 0.0, // 3.3.3
    val nbrOfSeizure: Double? = 0.0 // 3.3.4
) {
    constructor(
        envActions: List<MissionEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getDurationInHours2(protectedSpeciesActionEntities(envActions)),
        nbrOfRedirectShip = getNbrRedirectShip(envActions),
        nbrOfSeizure = getNbrOfSeizure(envActions)
    ) {}
    companion object {
        private val protectedSpeciesControlPlanThemeIds = listOf(103);
        fun getNbrRedirectShip(envActions: List<MissionEnvActionEntity?>): Double {
            return 0.0;  //TODO Complete from MonitorEnv
        }

        fun getNbrOfSeizure(envActions: List<MissionEnvActionEntity?>): Double {
            return 0.0; //TODO Complete from MonitorEnv
        }

        fun protectedSpeciesActionEntities(envActions: List<MissionEnvActionEntity?>): List<MissionEnvActionEntity?> {

            val protectedSpeciesActions = envActions.filter {
                it?.controlPlans?.map { c -> c.themeId }
                    ?.intersect(protectedSpeciesControlPlanThemeIds)?.isEmpty() == false
            }
            return protectedSpeciesActions;
        }

    }
}
