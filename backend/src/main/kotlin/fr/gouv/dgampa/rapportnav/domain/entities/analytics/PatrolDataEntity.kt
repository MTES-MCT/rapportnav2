package fr.gouv.dgampa.rapportnav.domain.entities.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import java.time.Instant
import java.util.UUID

data class OperationalSummaryEntity(
    val proFishingSeaSummary: LinkedHashMap<String, Map<String, Int?>>, // pêche - contrôles en mer
    val proFishingLandSummary: LinkedHashMap<String, Map<String, Int?>>, // pêche - contrôles à terre / débarque
    val proSailingSeaSummary: Map<String, Int>,
    val proSailingLandSummary: Map<String, Int>,
    val leisureSailingSeaSummary: Map<String, Int>,
    val leisureSailingLandSummary: Map<String, Int>,
    val leisureFishingSummary: Map<String, Int>,
    val envSummary: Map<String, Any>, // police environnement marin
)

data class PatrolDataEntity(
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
    val activity: Map<String, Map<String, Double>>? = null,
    val operationalSummary: OperationalSummaryEntity? = null
)

data class ThemeStats(
    val id: Int,
    val theme: String,
    val nbActions: Int,
    val durationInHours: Double
)
