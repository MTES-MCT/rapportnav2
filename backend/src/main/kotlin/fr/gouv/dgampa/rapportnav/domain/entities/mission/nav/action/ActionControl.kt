package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.*
import java.time.ZonedDateTime
import java.util.*

data class ActionControl(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val controlMethod: ControlMethod?,
//    val geom: MultiPolygon? = null,
    val observations: String? = null,
    val vesselIdentifier: String? = null,
    val vesselType: VesselTypeEnum? = null,
    val vesselSize: VesselSizeEnum? = null,
    val identityControlledPerson: String? = null,
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
}
