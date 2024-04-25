package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.ActionCompletionEnum
import org.locationtech.jts.geom.Geometry
import org.n52.jackson.datatype.jts.GeometryDeserializer
import java.time.ZonedDateTime
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
    open val actionStartDateTimeUtc: ZonedDateTime? = null,
    open val actionEndDateTimeUtc: ZonedDateTime? = null,
    open val department: String? = null,
    open val facade: String? = null,
    open val completedBy: String? = null,
    open val completion: ActionCompletionEnum? = null,
    open val controlPlans: List<EnvActionControlPlanEntity>? = listOf(),
    @JsonDeserialize(using = GeometryDeserializer::class) open val geom: Geometry? = null,
    open val isAdministrativeControl: Boolean? = null,
    open val isComplianceWithWaterRegulationsControl: Boolean? = null,
    open val isSafetyEquipmentAndStandardsComplianceControl: Boolean? = null,
    open val isSeafarersControl: Boolean? = null,
    open val openBy: String? = null,
)
