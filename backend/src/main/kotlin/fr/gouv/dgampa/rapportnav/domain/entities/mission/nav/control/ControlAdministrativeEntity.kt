package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.time.ZonedDateTime
import java.util.*

data class ControlAdministrativeEntity(
    val id: UUID,
    val missionId: Int,
    val actionControlId: String,
    val amountOfControls: Int,
    val unitShouldConfirm: Boolean?,
    val unitHasConfirmed: Boolean?,
    val compliantOperatingPermit: ControlResult?,
    val upToDateNavigationPermit: ControlResult?,
    val compliantSecurityDocuments: ControlResult?,
    val observations: String?,
    val infractions: List<InfractionEntity>? = null
) {

}
