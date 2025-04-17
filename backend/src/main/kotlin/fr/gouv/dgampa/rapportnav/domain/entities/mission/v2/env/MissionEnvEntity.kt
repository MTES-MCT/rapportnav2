package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.env

import fr.gouv.dgampa.rapportnav.domain.entities.Patchable
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionEntity
import org.locationtech.jts.geom.MultiPolygon
import java.time.Instant

data class MissionEnvEntity(
    val id: String? = null,
    val missionTypes: List<MissionTypeEnum>,
    val controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    val openBy: String? = null,
    val completedBy: String? = null,
    @Patchable
    var observationsByUnit: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    @Patchable
    var startDateTimeUtc: Instant,
    @Patchable
    var endDateTimeUtc: Instant? = null,
    val createdAtUtc: Instant? = null,
    val updatedAtUtc: Instant? = null,
    val envActions: List<EnvActionEntity>? = listOf(),
    val isGeometryComputedFromControls: Boolean? = false,
    val missionSource: MissionSourceEnum? = MissionSourceEnum.RAPPORT_NAV,
    val hasMissionOrder: Boolean? = false,
    val isUnderJdp: Boolean? = false,
    val isDeleted: Boolean? = false
) {
}
