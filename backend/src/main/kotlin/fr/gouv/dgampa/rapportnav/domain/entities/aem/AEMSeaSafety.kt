package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

data class AEMSeaSafety(
    val nbrOfHourInSea: Int, // 5.1
    val nbrOfHourPublicOrder: Int, // 5.3
    val nbrOfPublicOrderOperation: Int, // 5.4
) {
    constructor(
        vigimerActions: List<ActionVigimerEntity?>,
        nauticalActions: List<ActionNauticalEventEntity?>,
        baaemActions: List<ActionBAAEMPermanenceEntity?>,
        publicActions: List<ActionPublicOrderEntity?>
    ) : this(
        nbrOfPublicOrderOperation = publicActions.size,
        nbrOfHourPublicOrder = AEMSeaSafety.getNbrOfHourPublicOrder(publicActions),
        nbrOfHourInSea = AEMSeaSafety.getNbrOfHourInSea(vigimerActions, nauticalActions, baaemActions, publicActions),
    ) {
    }

    companion object {
        fun getNbrOfHourInSea(
            vigimerActions: List<ActionVigimerEntity?>,
            nauticalActions: List<ActionNauticalEventEntity?>,
            baaemActions: List<ActionBAAEMPermanenceEntity?>,
            publicActions: List<ActionPublicOrderEntity?>
        ): Int {
            //startDateTimeUtc = actionRescue.startDateTimeUtc,
            //endDateTimeUtc = actionRescue.endDateTimeUtc
            return 0;
        }

        fun getNbrOfHourPublicOrder(publicActions: List<ActionPublicOrderEntity?>): Int {
            //Reduce Action with SUM OF actionRescues.numberPersonsRescued
            return 0;
        }
    }
}
