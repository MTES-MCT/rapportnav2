
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.NavActionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action.NavActionRescue
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime
import java.util.*

data class ActionRescueEntity(
    val id: UUID,
    val missionId: Int,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val geom: MultiPolygon? = null,
    val isVesselRescue: Boolean = false,
    val isPersonRescue: Boolean = false,
    val isVesselNoticed: Boolean = false,
    val isVesselTowed: Boolean = false,
    val isInSRRorFollowedByCROSSMRCC: Boolean? = false,
    val numberPersonsRescued: Int?,
    val numberOfDeaths: Int?,
    val operationFollowsSRR: Boolean = false,
    val observations: String?,
    val locationDescription: String? = null
){
    fun toNavAction(): NavActionEntity {
        return NavActionEntity(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            actionType = ActionType.RESCUE,
            rescueAction = this
        )
    }

    fun toNavActionRescue(): NavActionRescue {
        return NavActionRescue(
            id = id,
            missionId = missionId,
            startDateTimeUtc = startDateTimeUtc,
            endDateTimeUtc = endDateTimeUtc,
            isVesselTowed = isVesselTowed,
            isVesselServedNotice = isVesselNoticed,
            isVesselRescue = isVesselRescue,
            isPersonRescue = isPersonRescue,
            observations = observations,
            numberOfDeaths = numberOfDeaths,
            operationFollowsSRR = operationFollowsSRR,
            geom = geom,
            isInSRRorFollowedByCROSSMRCC = isInSRRorFollowedByCROSSMRCC,
            numberPersonsRescued = numberPersonsRescued
        )
    }
}
