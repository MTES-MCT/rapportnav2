package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionReportTypeEnum

/**
 * Filter criteria for the mission **list** endpoint. Each field is optional and multi-select: a mission
 * matches when its value is contained in the provided list (OR within a field, AND across fields). A null
 * or empty list means that dimension is not filtered. Applied in-memory by [GetMissionList] against the
 * already-computed [fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.MissionListItem] projection.
 */
data class MissionListFilter(
    val statuses: List<MissionStatusEnum>? = null,
    val completenessStatuses: List<CompletenessForStatsStatusEnum>? = null,
    val reportTypes: List<MissionReportTypeEnum>? = null,
)
