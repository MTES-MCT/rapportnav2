package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import java.time.ZonedDateTime
import java.util.*

data class InfractionEntity(
    val id: UUID,
    var missionId: Int,
    var controlAdministrative: ControlAdministrativeEntity? = null, // Use ControlAdministrativeEntity instead of controlId
    var controlType: ControlType,
    var formalNotice: Boolean? = null,
    var observations: String? = null,
    var natinfs: List<NatinfEntity>? = null,
)
