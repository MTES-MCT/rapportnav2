package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class CreateOrUpdateMissionDataInput(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
    var controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: ZonedDateTime,
    val endDateTimeUtc: ZonedDateTime? = null,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean = false,
    val isGeometryComputedFromControls: Boolean = false,
) {
    fun toMissionEntity(): MissionEnvEntity {
        return MissionEnvEntity(
            id = this.id,
            missionTypes = this.missionTypes,
            controlUnits = this.controlUnits,
            openBy = this.openBy,
            completedBy = this.completedBy,
            observationsCacem = this.observationsCacem,
            observationsCnsp = this.observationsCnsp,
            facade = this.facade,
            geom = this.geom,
            startDateTimeUtc = this.startDateTimeUtc,
            endDateTimeUtc = this.endDateTimeUtc,
            isDeleted = false,
            missionSource = this.missionSource,
            hasMissionOrder = this.hasMissionOrder,
            isUnderJdp = this.isUnderJdp,
            isGeometryComputedFromControls = this.isGeometryComputedFromControls,
        )
    }
}
