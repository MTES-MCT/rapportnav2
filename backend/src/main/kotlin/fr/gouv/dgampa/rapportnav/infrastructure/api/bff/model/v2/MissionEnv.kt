package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionEnv(
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

    companion object {
        fun fromMissionEntity(mission: MissionEnvEntity): MissionEnv {
            return MissionEnv(
                id = mission.id,
                missionSource = mission.missionSource,
                startDateTimeUtc = mission.startDateTimeUtc,
                endDateTimeUtc = mission.endDateTimeUtc,
                openBy = mission.openBy,
                controlUnits = mission.controlUnits,
                hasMissionOrder = mission.hasMissionOrder,
                missionTypes = mission.missionTypes,
            )
        }
    }
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
