package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.NavActionControl
import java.time.ZonedDateTime
import java.util.*

data class ActionControl(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val controlMethod: ControlMethod?,
    val observations: String? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
    val identityControlledPerson: String? = null,
    val deletedAt: ZonedDateTime? = null,
    val controlAdministrative: ControlAdministrative?,
    val controlGensDeMer: ControlGensDeMer?,
    val controlNavigation: ControlNavigation?,
    val controlSecurity: ControlSecurity?
) {
    fun toNavAction(): NavAction {
        return NavAction(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.CONTROL,
            controlAction = this
        )
    }

    fun toNavActionControl(): NavActionControl {
        return NavActionControl(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            latitude = latitude,
            longitude = longitude,
            controlMethod = controlMethod,
            observations = observations,
            vesselIdentifier = vesselIdentifier,
            vesselType = vesselType,
            vesselSize = vesselSize,
            identityControlledPerson = identityControlledPerson,
            controlAdministrative = controlAdministrative,
            controlGensDeMer = controlGensDeMer,
            controlNavigation = controlNavigation,
            controlSecurity = controlSecurity,
        )
    }
}
