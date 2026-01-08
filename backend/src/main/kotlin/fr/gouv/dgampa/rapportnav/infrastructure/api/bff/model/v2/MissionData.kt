package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import com.fasterxml.jackson.annotation.JsonProperty
import fr.gouv.dgampa.rapportnav.config.JtsGeometrySerializer
import fr.gouv.dgampa.rapportnav.config.JtsMultiPolygonDeserializer
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import org.locationtech.jts.geom.MultiPolygon
import tools.jackson.databind.annotation.JsonDeserialize
import tools.jackson.databind.annotation.JsonSerialize
import java.time.Instant

data class MissionData(
    val missionTypes: List<MissionTypeEnum>,
    var controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    @field:JsonProperty("geom")
    @param:JsonDeserialize(using = JtsMultiPolygonDeserializer::class)
    @get:JsonSerialize(using = JtsGeometrySerializer::class)
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: Instant? = null,
    val endDateTimeUtc: Instant? = null,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean = false,
    val isDeleted: Boolean = false,
    val isGeometryComputedFromControls: Boolean = false,
    val observationsByUnit: String? = null
    ) {

    companion object {
        fun fromMissionEntity(mission: MissionEntity): MissionData {
            return MissionData(
                missionTypes = mission.missionTypes ?: listOf(),
                controlUnits = mission.controlUnits,
                openBy = mission.openBy,
                completedBy = mission.completedBy,
                observationsCacem = mission.observationsCacem,
                observationsCnsp = mission.observationsCnsp,
                facade = mission.facade,
                geom = mission.geom,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                missionSource = mission.missionSource,
                hasMissionOrder = mission.hasMissionOrder,
                isUnderJdp = mission.isUnderJdp,
                observationsByUnit = mission.observationsByUnit,
                isGeometryComputedFromControls = mission.isGeometryComputedFromControls
            )
        }
    }
}
