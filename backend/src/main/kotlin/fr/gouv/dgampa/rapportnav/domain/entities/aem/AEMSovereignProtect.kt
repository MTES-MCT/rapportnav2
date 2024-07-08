package fr.gouv.dgampa.rapportnav.domain.entities.aem

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity

data class AEMSovereignProtect (
    val anchoredActions: List<ActionStatusEntity?>,
    val navigationActions: List<ActionStatusEntity?>
){
}
