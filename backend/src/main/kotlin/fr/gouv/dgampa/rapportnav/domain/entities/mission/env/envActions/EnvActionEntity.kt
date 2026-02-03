package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.dgampa.rapportnav.config.JtsGeometryDeserializer
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.tags.TagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import org.locationtech.jts.geom.Geometry
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.Instant
import java.util.*

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "actionType",
    visible = true,
)
@JsonSubTypes(
    JsonSubTypes.Type(EnvActionControlEntity::class, name = "CONTROL"),
    JsonSubTypes.Type(EnvActionSurveillanceEntity::class, name = "SURVEILLANCE"),
    JsonSubTypes.Type(EnvActionNoteEntity::class, name = "NOTE"),
)
abstract class EnvActionEntity(
    open val id: UUID,
    open val actionType: ActionTypeEnum,
    open val actionStartDateTimeUtc: Instant? = null,
    open val actionEndDateTimeUtc: Instant? = null,
    open val department: String? = null,
    open val facade: String? = null,
    open val completedBy: String? = null,
    open val completion: ActionCompletionEnum? = null,
    @param:JsonProperty("geom")
    @param:JsonDeserialize(using = JtsGeometryDeserializer::class)
    @get:JsonSerialize(using = JtsGeometrySerializer::class)
    open val geom: Geometry? = null,
    open val isAdministrativeControl: Boolean? = null,
    open val isComplianceWithWaterRegulationsControl: Boolean? = null,
    open val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    open val isSeafarersControl: Boolean? = null,
    open val openBy: String? = null,
    open val observations: String? = null,
    open val observationsByUnit: String? = null,
    open val actionNumberOfControls: Int? = null,
    open val actionTargetType: ActionTargetTypeEnum? = null,
    open val vehicleType: VehicleTypeEnum? = null,
    open val infractions: List<InfractionEnvEntity>? = listOf(),
    open val coverMissionZone: Boolean? = null,
    open val tags: List<TagEntity>? = listOf(),
    open var themes: List<ThemeEntity>? = listOf(),
)
