package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish

import java.time.Instant

/**
@see monitorenv/backend/src/main/kotlin/fr/gouv/cacem/monitorenv/domain/entities/missions/MissionEntity.kt
for the full entity structure
 */
data class Mission(
    val id: Int,
    val missionTypes: List<MissionType>,
    val openBy: String? = null,
    val observationsCacem: String? = null,
    val observationsCnsp: String? = null,
    val facade: String? = null,
    val geom: MultiPolygon? = null,
    val startDateTimeUtc: Instant,
    val endDateTimeUtc: Instant? = null,
    val missionSource: MissionSource,
    val hasMissionOrder: Boolean? = false,
    val isUnderJdp: Boolean? = false,
    val controlUnits: List<ControlUnit> = listOf(),
)
