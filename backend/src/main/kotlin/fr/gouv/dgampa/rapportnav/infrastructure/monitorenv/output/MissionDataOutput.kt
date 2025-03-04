package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionDataOutput @JsonCreator(mode = JsonCreator.Mode.PROPERTIES) constructor(
    @JsonProperty("id") val id: Int,
    @JsonProperty("missionTypes") val missionTypes: List<MissionTypeEnum>,
    @JsonProperty("controlUnits") val controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    @JsonProperty("openBy") val openBy: String? = null,
    @JsonProperty("completedBy") val completedBy: String? = null,
    @JsonProperty("observationsCacem") val observationsCacem: String? = null,
    @JsonProperty("observationsCnsp") val observationsCnsp: String? = null,
    @JsonProperty("facade") val facade: String? = null,
    @JsonProperty("geom") val geom: MultiPolygon? = null,
    @JsonProperty("startDateTimeUtc") val startDateTimeUtc: ZonedDateTime,
    @JsonProperty("endDateTimeUtc") val endDateTimeUtc: ZonedDateTime? = null,
    @JsonProperty("envActions") val envActions: List<EnvActionEntity>? = null,
    @JsonProperty("missionSource") val missionSource: MissionSourceEnum,
    @JsonProperty("hasMissionOrder") val hasMissionOrder: Boolean,
    @JsonProperty("isUnderJdp") val isUnderJdp: Boolean,
    @JsonProperty("isGeometryComputedFromControls") val isGeometryComputedFromControls: Boolean,
    @JsonProperty("observationsByUnit") val observationsByUnit: String? = null
) {

    fun toMissionEntity(): MissionEntity {
        return MissionEntity(
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
            observationsByUnit = observationsByUnit
        )
    }
}
