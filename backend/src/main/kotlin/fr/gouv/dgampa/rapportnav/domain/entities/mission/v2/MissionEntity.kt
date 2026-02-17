package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.CompletenessForStatsStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionStatusEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionEnvEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.format.DateTimeParseException
import java.util.*

data class MissionEntity(
    val id: Int? = null,
    val idUUID: UUID? = null,
    val data: MissionEnvEntity? = null,
    val actions: List<MissionActionEntity>? = listOf(),
    val generalInfos: MissionGeneralInfoEntity2? = null
) {
    private val logger = LoggerFactory.getLogger(MissionEntity::class.java)

    fun isCompleteForStats(): CompletenessForStatsEntity {
        val completenessForStats = this.actionsIsCompleteForStats()

        // for secondary missions (missions conjointes ou inter-services)
        // do not validate generalInfos, only actions matter
        val isGeneralInfoComplete = if (this.isInterServices()) {
            true
        } else {
            this.generalInfos?.isCompleteForStats()
        }

        return CompletenessForStatsEntity(
            status = if (isGeneralInfoComplete == true) completenessForStats.status  else CompletenessForStatsStatusEnum.INCOMPLETE,
            sources = if (isGeneralInfoComplete == true) completenessForStats.sources  else completenessForStats.sources?.plus(MissionSourceEnum.RAPPORT_NAV)
                ?.distinct(),
        )
    }

    fun calculateMissionStatus(
        startDateTimeUtc: Instant,
        endDateTimeUtc: Instant? = null,
    ): MissionStatusEnum {
        val compareDate = Instant.now()
        if (endDateTimeUtc == null || startDateTimeUtc == null) return MissionStatusEnum.UNAVAILABLE
        val endDateTime = Instant.parse(endDateTimeUtc.toString())
        val startDateTime = Instant.parse(startDateTimeUtc.toString())
        try {
            if (startDateTime.isBefore(compareDate) && endDateTime.isAfter(compareDate)) return MissionStatusEnum.IN_PROGRESS
            if (endDateTime.isBefore(compareDate) || endDateTime.equals(compareDate)) return MissionStatusEnum.ENDED
            if (startDateTime.isAfter(compareDate) || startDateTime.equals(compareDate))
                return MissionStatusEnum.UPCOMING
        } catch (e: DateTimeParseException) {
            logger.error("calculateMissionStatus - error with startDate: ${startDateTime}, endDate: ${endDateTime}", e)
        }
        return MissionStatusEnum.UNAVAILABLE
    }

    private fun actionsIsCompleteForStats(): CompletenessForStatsEntity {

        val sources: List<MissionSourceEnum> = this.actions
            ?.filter { it.isCompleteForStats != true }
            ?.flatMap { it.sourcesOfMissingDataForStats.orEmpty() }
            ?.distinct()
            ?: emptyList()

        val status = if (sources.distinct().isEmpty()) CompletenessForStatsStatusEnum.COMPLETE else CompletenessForStatsStatusEnum.INCOMPLETE

        return CompletenessForStatsEntity(
            status = status,
            sources = sources.distinct()
        )
    }

    // interservices means with other controlUnits
    private fun isInterServices(): Boolean =
        data?.controlUnits
            .orEmpty()
            .distinct()
            .take(2)     // stops early if more than 1 distinct value
            .count() == 2


}

