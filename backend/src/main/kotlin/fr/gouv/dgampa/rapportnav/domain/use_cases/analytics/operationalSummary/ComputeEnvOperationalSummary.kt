package fr.gouv.dgampa.rapportnav.domain.use_cases.analytics.operationalSummary

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.analytics.ThemeStats
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.themes.ThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.ComputeDurations
import fr.gouv.dgampa.rapportnav.domain.utils.ComputeDurationUtils
import kotlin.collections.flatMap
import kotlin.time.DurationUnit

@UseCase
class ComputeEnvOperationalSummary(
    private val computeDurations: ComputeDurations

) {

    fun execute(actions: List<MissionActionEntity>): Map<String, Any> {
        val filteredActions = actions
            .filterIsInstance<MissionEnvActionEntity>()

        val controls = filteredActions.filter { it.envActionType == ActionTypeEnum.CONTROL }
        val surveillances = filteredActions.filter { it.envActionType == ActionTypeEnum.SURVEILLANCE }

        val nbSurveillances = surveillances.size
        val totalSurveillanceDurationInHours = surveillances.sumOf<MissionEnvActionEntity> { surveillance ->
            ComputeDurationUtils.durationInHours(
                startDateTimeUtc = surveillance.startDateTimeUtc,
                endDateTimeUtc = surveillance.endDateTimeUtc
            ).toInt()
        }

        val nbControls = controls.size
        val nbInfractionsWithRecord = controls.sumOf {
            it.envInfractions?.count { inf -> inf.infractionType == InfractionTypeEnum.WITH_REPORT } ?: 0
        }
        val nbInfractionsWithoutRecord = controls.sumOf {
            it.envInfractions?.count { inf -> inf.infractionType == InfractionTypeEnum.WITHOUT_REPORT } ?: 0
        }



        val controlThemes = computeThemeStats(controls)
        val surveillanceThemes = computeThemeStats(surveillances)


        val summary = mapOf(
            "nbControls" to nbControls,
            "nbSurveillances" to nbSurveillances,
            "totalSurveillanceDurationInHours" to totalSurveillanceDurationInHours,
            "nbInfractionsWithRecord" to nbInfractionsWithRecord,
            "nbInfractionsWithoutRecord" to nbInfractionsWithoutRecord,
            "controlThemes" to controlThemes,
            "surveillanceThemes" to surveillanceThemes,
        )


        return summary
    }


    fun flattenThemes(themes: List<ThemeEntity>): List<ThemeEntity> =
        themes.flatMap { theme ->
            listOf(theme) + flattenThemes(theme.subThemes)
        }

    fun computeThemeStats(actions: List<MissionEnvActionEntity>): List<ThemeStats> {
        val stats = mutableMapOf<Pair<Int, String>, ThemeStats>()

        for (action in actions) {
            // Compute the duration (in hours) of this action
            val durationHours = action.startDateTimeUtc?.let { start ->
                action.endDateTimeUtc?.let { end ->
                    computeDurations.convertFromSeconds(
                        computeDurations.durationInSeconds(start, end) ?: 0,
                        DurationUnit.HOURS
                    )

                }
            } ?: 0.0

            // Flatten nested subthemes and accumulate
            for (theme in flattenThemes(action.themes.orEmpty())) {
                val key = theme.id to theme.name
                val current = stats[key]
                if (current == null) {
                    stats[key] = ThemeStats(theme.id, theme.name, 1, durationHours)
                } else {
                    stats[key] = current.copy(
                        nbActions = current.nbActions + 1,
                        durationInHours = current.durationInHours + durationHours
                    )
                }
            }
        }

        return stats.values.sortedBy { it.id }
    }



}
