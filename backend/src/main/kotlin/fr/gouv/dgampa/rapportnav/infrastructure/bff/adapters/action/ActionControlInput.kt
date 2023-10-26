package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlMethod
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlNavigation
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.ControlSecurity
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

data class ActionControlInput(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: String,
    val endDateTimeUtc: String?,
    val latitude: Double?,
    val longitude: Double?,
    val controlMethod: ControlMethod?,
    val vesselType: VesselTypeEnum?,
    val vesselIdentifier: String?,
    val vesselSize: VesselSizeEnum?,
    val identityControlledPerson: String?,
    val observations: String?,
    val controlAdministrative: ControlAdministrative?,
    val controlGensDeMer: ControlGensDeMer?,
    val controlNavigation: ControlNavigation?,
    val controlSecurity: ControlSecurity?
) {
    fun toActionControl(): ActionControlEntity {
        return ActionControlEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = ZonedDateTime.parse(startDateTimeUtc, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"))),
            endDateTimeUtc = endDateTimeUtc?.let {
                ZonedDateTime.parse(it, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC")))
            } ?: ZonedDateTime.parse(startDateTimeUtc, DateTimeFormatter.ISO_INSTANT.withZone(ZoneId.of("UTC"))),
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            vesselType = vesselType,
            vesselIdentifier = vesselIdentifier,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            observations = observations,
            controlAdministrative = controlAdministrative?.toControlAdministrativeEntity(missionId = missionId, actionId = id),
            controlGensDeMer = controlGensDeMer?.toControlGensDeMerEntity(missionId = missionId, actionId = id),
            controlNavigation = controlNavigation?.toControlNavigationEntity(missionId = missionId, actionId = id),
            controlSecurity = controlSecurity?.toControlSecurityEntity(missionId = missionId, actionId = id),
        )
    }
}
