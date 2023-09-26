package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlEquipmentAndSecurity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlNavigationRules
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime
import java.util.*

data class ActionControl(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime?,
    val geom: MultiPolygon? = null,
    val observations: String? = null,
    val vesselIdentifier: String? = null,
    val vesselSize: String? = null,
    val identityControlledPerson: String? = null,
    val controlsVesselAdministrative: ControlVesselAdministrative?,
    val controlsGensDeMer: ControlGensDeMer?,
    val controlsNavigationRules: ControlNavigationRules?,
    val controlsEquipmentAndSecurity: ControlEquipmentAndSecurity?
) {
    fun toNavAction(): NavAction {
        return NavAction(
            id = id,
            missionId = missionId,
            actionStartDateTimeUtc = startDateTimeUtc,
            actionEndDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.CONTROL,
            controlAction = this
        )
    }
}
