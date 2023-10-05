package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControl
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class ActionControlInput(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: String,
    val endDateTimeUtc: String?,
    val controlMethod: ControlMethod?,
    val vesselType: VesselTypeEnum?,
    val vesselIdentifier: String?,
    val vesselSize: VesselSizeEnum?,
    val identityControlledPerson: String?,
    val observations: String?,
) {
    fun toActionControl(): ActionControl {
        return ActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = ZonedDateTime.parse(startDateTimeUtc, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"))),
            endDateTimeUtc = endDateTimeUtc?.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")))
            } ?: ZonedDateTime.parse(startDateTimeUtc, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"))),
            controlMethod = controlMethod,
            vesselType = vesselType,
            vesselIdentifier = vesselIdentifier,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            observations = observations,
            controlAdministrative = null,
            controlGensDeMer = null,
            controlNavigation = null,
            controlSecurity = null,
        )
    }
}
