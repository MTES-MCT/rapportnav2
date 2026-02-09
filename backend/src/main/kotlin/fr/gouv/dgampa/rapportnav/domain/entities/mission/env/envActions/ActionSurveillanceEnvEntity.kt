package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.config.JtsMultiPolygonDeserializer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import org.locationtech.jts.geom.Geometry
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import java.util.*

data class ActionSurveillanceEnvEntity(
    override val id: UUID,
    override val actionStartDateTimeUtc: Instant? = null,
    override val actionEndDateTimeUtc: Instant? = null,
    @param:JsonProperty("geom")
    @param:JsonDeserialize(using = JtsMultiPolygonDeserializer::class)
    @get:JsonSerialize(using = JtsGeometrySerializer::class)
    override val geom: Geometry? = null,
    override val facade: String? = null,
    override val department: String? = null,
    override val completedBy: String? = null,
    override val completion: ActionCompletionEnum? = null,
    override val controlPlans: List<EnvActionControlPlanEntity>? = listOf(),
    override val openBy: String? = null,
    override val observations: String? = null,
    override val observationsByUnit: String? = null,
    override val coverMissionZone: Boolean? = null,
    override val tags: List<TagEntity>? = listOf(),
    override var themes: List<ThemeEntity>? = listOf(),
) : ActionEnvEntity(
    actionType = ActionTypeEnum.SURVEILLANCE,
    id = id,
)
