package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime
import java.util.*

data class NavActionRescue(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val geom: MultiPolygon? = null,
    val observations: String? = null,
    val isVesselRescue: Boolean = false,
    val isPersonRescue: Boolean = false,
    val isVesselTowed: Boolean = false,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val isVesselServedNotice: Boolean = false,
    val numberPersonsRescued: Int? = null,
    val numberOfDeaths: Int? = null,
    val operationFollowsDEFREP: Boolean = false,
    val locationDescription: String? = null

): ActionData()
