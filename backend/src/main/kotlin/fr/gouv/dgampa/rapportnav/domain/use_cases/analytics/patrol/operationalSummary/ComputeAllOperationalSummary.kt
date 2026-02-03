package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.patrol.operationalSummary

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.OperationalSummaryEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEntity
import kotlin.collections.orEmpty

@UseCase
class ComputeAllOperationalSummary(
    private val computeEnvOperationalSummary: ComputeEnvOperationalSummary,
    private val computeSailingOperationalSummary: ComputeSailingOperationalSummary,
    private val computeFishingOperationalSummary: ComputeFishingOperationalSummary,
) {

    fun execute(mission: MissionEntity?): OperationalSummaryEntity {

        val proFishingSeaSummary: LinkedHashMap<String, Map<String, Int?>>
            = computeFishingOperationalSummary.getProFishingSeaSummary(mission?.actions.orEmpty())
        val proFishingLandSummary: LinkedHashMap<String, Map<String, Int?>>
            = computeFishingOperationalSummary.getProFishingLandSummary(mission?.actions.orEmpty())

        val proSailingSeaSummary: Map<String, Int> = computeSailingOperationalSummary.getProSailingSeaSummary(mission?.actions.orEmpty())
        val proSailingLandSummary: Map<String, Int> = computeSailingOperationalSummary.getProSailingLandSummary(mission?.actions.orEmpty())

        val leisureSailingSeaSummary: Map<String, Int> = computeSailingOperationalSummary.getLeisureSailingSeaSummary(mission?.actions.orEmpty())
        val leisureSailingLandSummary: Map<String, Int> = computeSailingOperationalSummary.getLeisureSailingLandSummary(mission?.actions.orEmpty())

        val leisureFishingSummary: Map<String, Int> = computeFishingOperationalSummary.getLeisureFishingSummary(mission?.actions.orEmpty())

        val envSummary: Map<String, Any> = computeEnvOperationalSummary.execute(mission?.actions.orEmpty())

        return OperationalSummaryEntity(
            proFishingSeaSummary = proFishingSeaSummary,
            proFishingLandSummary = proFishingLandSummary,
            proSailingSeaSummary = proSailingSeaSummary,
            proSailingLandSummary = proSailingLandSummary,
            leisureSailingSeaSummary = leisureSailingSeaSummary,
            leisureSailingLandSummary = leisureSailingLandSummary,
            leisureFishingSummary = leisureFishingSummary,
            envSummary = envSummary,
        )
    }

}
