package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env

import fr.gouv.dgampa.rapportnav.domain.entities.Patchable
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.ZonedDateTime

data class MissionEnvEntity(
    val id: Int? = null,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<LegacyControlUnitEntity> = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    @Patchable
    var observationsByUnit: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    @Patchable
    var startDateTimeUtc: ZonedDateTime,
    @Patchable
    var endDateTimeUtc: ZonedDateTime? = null,
    val createdAtUtc: ZonedDateTime? = null,
    val updatedAtUtc: ZonedDateTime? = null,
    val envActions: List<EnvActionEntity>? = listOf(),
    val isDeleted: Boolean,
    val isGeometryComputedFromControls: Boolean,
    val missionSource: MissionSourceEnum,
    val hasMissionOrder: Boolean,
    val isUnderJdp: Boolean,
)
