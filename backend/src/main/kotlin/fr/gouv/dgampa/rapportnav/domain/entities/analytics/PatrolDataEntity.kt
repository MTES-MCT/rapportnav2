package fr.gouv.dgampa.rapportnav.domain.entities.analytics

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlResources.LegacyControlUnitEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.NavActionInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import java.time.Instant
import java.util.UUID

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
    val generalInfos: MissionGeneralInfoEntity2? = null,
    val completenessForStats: CompletenessForStatsEntity? = null,
    val isMissionFinished: Boolean? = null,
    val activity: Map<String, Map<String, Double>>? = null, // activité du navire
    val operationalSummary: OperationalSummaryEntity? = null, // bilan opérationnel
    val controlPolicies: ControlPoliciesEntity? = null, // contrôles par politique publique
    val otherActionsSummary: Map<String, NavActionInfoEntity?>? = null, // Autres missions
    val internTrainingSummary: Map<String, Int>? = null, // Soutien à la politique de formation
)

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

data class ControlPoliciesEntity(
    val proFishing: ControlPolicyData? = null,
    val security: ControlPolicyData? = null,
    val navigation: ControlPolicyData? = null,
    val gensDeMer: ControlPolicyData? = null,
    val administrative: ControlPolicyData? = null,
    val envPollution: ControlPolicyData? = null,
    val other: ControlPolicyData? = null,
)

data class ControlPolicyData(
    val nbControls: Int = 0,
    val nbControlsSea: Int? = 0,
    val nbControlsLand: Int? = 0,
    val nbInfractionsWithRecord: Int = 0,
    val nbInfractionsWithoutRecord: Int = 0,
)


data class ThemeStats(
    val id: Int,
    val theme: String,
    val nbActions: Int,
    val durationInHours: Double
)

