package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env.MissionEnvEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionEnvDataOutput(
    override val id: Int,
    override val missionTypes: List<MissionTypeEnum>,
    override val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    override val openBy: String? = null,
    override val completedBy: String? = null,
    override val observationsByUnit: String? = null,
    override val observationsCacem: String? = null,
    override val observationsCnsp: String? = null,
    override val facade: String? = null,
    override val geom: MultiPolygon? = null,
    override val startDateTimeUtc: ZonedDateTime,
    override val endDateTimeUtc: ZonedDateTime? = null,
    override val createdAtUtc: ZonedDateTime? = null,
    override val updatedAtUtc: ZonedDateTime? = null,
    override val envActions: List<MissionEnvActionDataOutput>? = null,
    override val missionSource: MissionSourceEnum,
    override val hasMissionOrder: Boolean,
    override val isUnderJdp: Boolean,
    override val isGeometryComputedFromControls: Boolean,
) : MissionEnvOutput {

    fun toMissionEnvEntity(): MissionEnvEntity {
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
