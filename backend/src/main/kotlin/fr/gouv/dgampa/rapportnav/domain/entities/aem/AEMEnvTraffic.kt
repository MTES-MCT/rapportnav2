package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionRescueEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ExtendedEnvActionEntity

data class AEMEnvTraffic(
    val nbrOfHourInSea: Int, //3.3.1
    val nbrOfRedirectShip: Int, // 3.3.3
    val nbrOfSeizure: Int // 3.3.4
) {
    constructor(
        envActions: List<ExtendedEnvActionEntity?>
    ) : this(
        nbrOfHourInSea = AEMEnvTraffic.getNbrOfHourInSea(envActions),
        nbrOfRedirectShip = AEMEnvTraffic.getNbrRedirectShip(envActions),
        nbrOfSeizure = AEMEnvTraffic.getNbrOfSeizure(envActions)
    ) {

    }
    companion object {
        fun getNbrOfHourInSea(envActions: List<ExtendedEnvActionEntity?>): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrRedirectShip(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

        fun getNbrOfSeizure(envActions: List<ExtendedEnvActionEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }

    }
}
