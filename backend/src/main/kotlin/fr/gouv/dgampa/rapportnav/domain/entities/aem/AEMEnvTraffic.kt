package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.utils.AEMUtils
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils

data class AEMEnvTraffic(
    val nbrOfHourAtSea: Int? = 0, //3.3.1
    val nbrOfRedirectShip: Int? = 0, // 3.3.3
    val nbrOfSeizure: Int? = 0 // 3.3.4
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourAtSea = AEMUtils.getEnvDurationInHours(protectedSpeciesActionEntities(envActions)).toInt(),
        nbrOfRedirectShip = getNbrRedirectShip(envActions),
        nbrOfSeizure = getNbrOfSeizure(envActions)
    ) {

    }

    companion object {
        val protectedSpeciesControlPlanThemeIds = listOf(103);
        fun getNbrRedirectShip(envActions: List<ExtendedEnvActionEntity?>): Int {
            return 0;  //TODO Complete from MonitorEnv
        }

        fun getNbrOfSeizure(envActions: List<ExtendedEnvActionEntity?>): Int {
            return 0; //TODO Complete from MonitorEnv
        }

        fun protectedSpeciesActionEntities(envActions: List<ExtendedEnvActionEntity?>): List<ExtendedEnvActionEntity?> {

            val protectedSpeciesActions = envActions.filter {
                it?.controlAction?.action?.controlPlans?.map { c -> c.themeId }
                    ?.containsAll(protectedSpeciesControlPlanThemeIds) == true ||
                    it?.surveillanceAction?.action?.controlPlans?.map { c -> c.themeId }
                        ?.containsAll(protectedSpeciesControlPlanThemeIds) == true
            }
            return protectedSpeciesActions;
        }

    }
}
