package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.control.NavActionControl
import java.time.ZonedDateTime
import java.util.*

data class ActionControlEntity(
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
    val controlAdministrative: ControlAdministrativeEntity?,
    val controlGensDeMer: ControlGensDeMerEntity?,
    val controlNavigation: ControlNavigationEntity?,
    val controlSecurity: ControlSecurityEntity?
) {
    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
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
            controlAdministrative = controlAdministrative?.toControlAdministrative(),
            controlGensDeMer = controlGensDeMer?.toControlGensDeMer(),
            controlNavigation = controlNavigation?.toControlNavigation(),
            controlSecurity = controlSecurity?.toControlSecurity(),
        )
    }
}
