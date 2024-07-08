package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.*

data class AEMSeaSafety (
    val vigimerActions: List<ActionVigimerEntity?>,
    val nauticalActions: List<ActionNauticalEventEntity?>,
    val baaemActions: List<ActionBAAEMPermanenceEntity?>,
    val publicActions: List<ActionPublicOrderEntity?>
){
}
