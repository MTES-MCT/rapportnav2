package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.v2.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

interface MissionEnvOutput {
    val id: Int
    val missionTypes: List<MissionTypeEnum>
    val controlUnits: List<LegacyControlUnitEntity>?
    val openBy: String?
    val completedBy: String?
    val observationsByUnit: String?
    val observationsCacem: String?
    val observationsCnsp: String?
    val facade: String?
    val geom: MultiPolygon?
    val startDateTimeUtc: ZonedDateTime
    val endDateTimeUtc: ZonedDateTime?
    val createdAtUtc: ZonedDateTime?
    val updatedAtUtc: ZonedDateTime?
    val envActions: List<MissionEnvActionDataOutput>?
    val missionSource: MissionSourceEnum
    val hasMissionOrder: Boolean
    val isUnderJdp: Boolean
    val isGeometryComputedFromControls: Boolean
}
