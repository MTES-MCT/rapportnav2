package fr.gouv.dgampa.rapportnav.infrastructure.api.public_api.analytics.v1.adapters.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import java.time.Instant
import java.util.UUID

data class AEMMetricOutput(
    val id: String,
    val title: String,
    val value: Double,
)

data class ApiAnalyticsAEMDataOutput(
    val id: Int? = null,
    val idUUID: UUID? = null,
    val serviceId: Int? = null,
    val missionTypes: List<MissionTypeEnum>? = listOf(),
    val controlUnits: List<LegacyControlUnitEntity>? = listOf(),
    val facade: String? = null,
    var startDateTimeUtc: Instant? = null,
    var endDateTimeUtc: Instant? = null,
    var isDeleted: Boolean? = null,
    val missionSource: MissionSourceEnum? = null,
    val data: List<AEMMetricOutput> = listOf(),

)
