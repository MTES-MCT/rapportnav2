package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class CreateOrUpdateMissionDataInput(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
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
    val isUnderJdp: Boolean,
    val isGeometryComputedFromControls: Boolean,
) {
    fun toMissionEntity(): MissionEntity {
        return MissionEntity(
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

    companion object {
        fun fromMissionEntity(missionEntity: MissionEntity): CreateOrUpdateMissionDataInput {
            return CreateOrUpdateMissionDataInput(
                id = missionEntity.id,
                missionTypes = missionEntity.missionTypes,
                controlUnits = missionEntity.controlUnits,
                openBy = missionEntity.openBy,
                completedBy = missionEntity.completedBy,
                observationsCacem = missionEntity.observationsCacem,
                observationsCnsp = missionEntity.observationsCnsp,
                facade = missionEntity.facade,
                geom = missionEntity.geom,
                startDateTimeUtc = missionEntity.startDateTimeUtc,
                endDateTimeUtc = missionEntity.endDateTimeUtc,
                missionSource = missionEntity.missionSource,
                hasMissionOrder = missionEntity.hasMissionOrder,
                isUnderJdp = missionEntity.isUnderJdp,
                isGeometryComputedFromControls = missionEntity.isGeometryComputedFromControls,
            )
        }
    }
}
