package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import org.locationtech.jts.geom.Geometry
import java.time.ZonedDateTime
import java.util.*

data class EnvActionSurveillanceEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: ZonedDateTime? = null,
    override val actionEndDateTimeUtc: ZonedDateTime? = null,
    override val geom: Geometry? = null,
    override val facade: String? = null,
    override val department: String? = null,
    override val completedBy: String? = null,
    override val completion: ActionCompletionEnum? = null,
    override val controlPlans: List<EnvActionControlPlanEntity>? = listOf(),
    override val openBy: String? = null,
    val themes: List<ThemeEntity>? = listOf(),
    val observations: String? = null,
    val coverMissionZone: Boolean? = null,
) : EnvActionEntity(
    actionType = ActionTypeEnum.SURVEILLANCE,
    id = id,
)
