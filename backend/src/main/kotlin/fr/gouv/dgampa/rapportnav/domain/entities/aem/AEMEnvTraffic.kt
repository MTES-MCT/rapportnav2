package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils

data class AEMEnvTraffic(
    val nbrOfHourAtSea: Double? = 0.0, //3.3.1
    val nbrOfRedirectShip: Double? = 0.0, // 3.3.3
    val nbrOfSeizure: Double? = 0.0 // 3.3.4
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(protectedSpeciesActionEntities(envActions)),
        nbrOfRedirectShip = getNbrRedirectShip(envActions),
        nbrOfSeizure = getNbrOfSeizure(envActions)
    ) {

    }

    companion object {
        private val protectedSpeciesControlPlanThemeIds = listOf(103);
        fun getNbrRedirectShip(envActions: List<ExtendedEnvActionEntity?>): Double {
            return 0.0;  //TODO Complete from MonitorEnv
        }

        fun getNbrOfSeizure(envActions: List<ExtendedEnvActionEntity?>): Double {
            return 0.0; //TODO Complete from MonitorEnv
        }

        fun protectedSpeciesActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {

            val protectedSpeciesActions = envActions.filter {
                it?.controlAction?.action?.controlPlans?.map { c -> c.themeId }
                    ?.intersect(protectedSpeciesControlPlanThemeIds)?.isEmpty() == false ||
                    it?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.intersect(protectedSpeciesControlPlanThemeIds)?.isEmpty() == false
            }
            return protectedSpeciesActions;
        }

    }
}
