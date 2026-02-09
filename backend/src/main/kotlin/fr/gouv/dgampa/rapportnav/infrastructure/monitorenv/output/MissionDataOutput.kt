package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.config.JtsMultiPolygonDeserializer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.ZonedDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class MissionDataOutput @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @field:JsonProperty("id") val id: Int,
    @field:JsonProperty("missionTypes") val missionTypes: List<MissionTypeEnum>,
    @field:JsonProperty("controlUnits") val controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    @field:JsonProperty("openBy") val openBy: String? = null,
    @field:JsonProperty("completedBy") val completedBy: String? = null,
    @field:JsonProperty("observationsCacem") val observationsCacem: String? = null,
    @field:JsonProperty("observationsCnsp") val observationsCnsp: String? = null,
    @field:JsonProperty("facade") val facade: String? = null,
    @field:JsonProperty("geom")
    @param:JsonDeserialize(using = JtsMultiPolygonDeserializer::class)
    @get:JsonSerialize(using = JtsGeometrySerializer::class)
    val geom: MultiPolygon? = null,
    @field:JsonProperty("startDateTimeUtc") val startDateTimeUtc: ZonedDateTime,
    @field:JsonProperty("endDateTimeUtc") val endDateTimeUtc: ZonedDateTime? = null,
    @field:JsonProperty("envActions") val envActions: List<ActionEnvEntity>? = null,
    @field:JsonProperty("missionSource") val missionSource: MissionSourceEnum,
    @field:JsonProperty("hasMissionOrder") val hasMissionOrder: Boolean,
    @field:JsonProperty("isUnderJdp") val isUnderJdp: Boolean,
    @field:JsonProperty("isGeometryComputedFromControls") val isGeometryComputedFromControls: Boolean,
    @field:JsonProperty("observationsByUnit") val observationsByUnit: String? = null
) {

    fun toMissionEnvEntity(): MissionEnvEntity {
        return MissionEnvEntity(
            id = id,
            missionTypes = missionTypes,
            controlUnits = controlUnits.orEmpty(),
            openBy = openBy,
            completedBy = completedBy,
            observationsCacem = observationsCacem,
            observationsCnsp = observationsCnsp,
            facade = facade,
            geom = geom,
            startDateTimeUtc = startDateTimeUtc.toInstant(),
            endDateTimeUtc = endDateTimeUtc?.toInstant(),
            envActions = envActions,
            missionSource = missionSource,
            hasMissionOrder = hasMissionOrder,
            isUnderJdp = isUnderJdp,
            isGeometryComputedFromControls = isGeometryComputedFromControls,
            isDeleted = false, // TODO this is weird,
            observationsByUnit = observationsByUnit
        )
    }
}
