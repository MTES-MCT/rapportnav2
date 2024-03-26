package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapActionStatusTypeToHumanString
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActionItem
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActions
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@UseCase
class FormatActionsForTimeline(
    private val groupActionByDate: GroupActionByDate,
) {

    fun formatTimeline(actions: List<MissionActionEntity>?): Map<LocalDate, List<String>>? {

        if (actions.isNullOrEmpty()) {
            return null
        }
        // Sort missions ASC
        val chronoActions = actions.reversed()
        
        // Group actions by date
        val groupedActions = groupActionByDate.execute(actions = chronoActions)

        // Map each group to list of formatted strings
        return groupedActions?.mapValues { (_, actionsOnDate) ->
            actionsOnDate.mapNotNull { action ->
                formatAction(action)
            }
        }
    }

    fun formatForRapportNav1(actions: Map<LocalDate, List<String>>?): List<TimelineActions> {
        return actions?.map { (date, actionsAsString) ->
            TimelineActions(
                date = date.toString(),
                freeNote = actionsAsString.map { TimelineActionItem(it) }
            )
        } ?: emptyList()
    }

    private fun formatAction(action: MissionActionEntity): String? {
        return when (action) {
            is MissionActionEntity.EnvAction -> formatEnvAction(action)
            is MissionActionEntity.FishAction -> formatFishAction(action)
            is MissionActionEntity.NavAction -> formatNavAction(action)
        }
    }

    private fun formatEnvAction(action: MissionActionEntity.EnvAction): String? {
        return action.envAction?.controlAction?.action?.let { envControlAction ->
            formatEnvControl(envControlAction)
        }
    }

    private fun formatFishAction(action: MissionActionEntity.FishAction): String? {
        return action.fishAction.controlAction?.action?.let { fishControlAction ->
            formatFishControl(fishControlAction)
        }
    }

    private fun formatNavAction(action: MissionActionEntity.NavAction): String? {
        val navAction = action.navAction
        return when (navAction.actionType) {
            ActionType.NOTE -> formatNavNote(navAction.freeNoteAction)
            ActionType.STATUS -> formatNavStatus(navAction.statusAction)
            ActionType.CONTROL -> formatNavControl(navAction.controlAction)
            else -> null
        }
    }


    fun formatTime(dateTime: ZonedDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "N/A"
    }


    fun formatEnvControl(action: EnvActionControlEntity?): String? {
        return action?.let {
            val startTime = formatTime(action.actionStartDateTimeUtc)
            val endTime = formatTime(action.actionEndDateTimeUtc)
            val facade = action.facade?.let { " - $it" } ?: ""
            val themes = action.themes?.let { " - ${it.map { theme -> theme.theme }.joinToString(" + ")}" } ?: ""
            val amountOfControls = action.actionNumberOfControls?.let { " - $it contrôle(s)" } ?: ""
            return "$startTime / $endTime - Contrôle Environnement$facade$themes$amountOfControls"
        }
    }

    fun formatFishControl(action: MissionAction?): String? {
        return action?.let {
            val startTime = formatTime(action.actionDatetimeUtc)
            val coords = "${action.latitude ?: "N/A"}/${action.longitude ?: "N/A"}"
            val vesselInfo = "${action.vesselName ?: "N/A"} - ${action.vesselId}"
            val seizureAndDiversion = action.seizureAndDiversion?.let { " - retour du navire au port" } ?: ""
            val natinfs: String = listOf(
                action.gearInfractions.map { it.natinf },
                action.logbookInfractions.map { it.natinf },
                action.speciesInfractions.map { it.natinf },
                action.otherInfractions.map { it.natinf }
            ).flatten().distinct().let { list ->
                if (list.isEmpty()) {
                    " - RAS"
                } else {
                    " - NATINF: ${list.filterNotNull().joinToString(" + ")}"
                }
            }
            val pvCount = listOf(
                action.gearInfractions.map { it.infractionType },
                action.logbookInfractions.map { it.infractionType },
                action.speciesInfractions.map { it.infractionType },
                action.otherInfractions.map { it.infractionType }
            ).flatten().count { it == InfractionType.WITH_RECORD }
            val pv = if (pvCount > 0) "avec PV" else "sans PV"

            val species = if (action.speciesOnboard.isNotEmpty()) {
                "Espèces contrôlées: " + action.speciesOnboard.joinToString(" - ") { "${it.speciesCode}: ${it.controlledWeight ?: "N/A"}/${it.declaredWeight ?: "N/A"} kg" }
            } else {
                "Espèces contrôlées: N/A"
            }

            return "$startTime - Contrôle Pêche - $coords - $vesselInfo - $species - Infractions: $pv$natinfs$seizureAndDiversion"
        }
    }

    fun formatNavNote(action: ActionFreeNoteEntity?): String? {
        return action?.let {
            val startTime = formatTime(action.startDateTimeUtc)
            val observation = action.observations ?: ""
            return "$startTime - $observation"
        }
    }


    fun formatNavStatus(action: ActionStatusEntity?): String? {
        return action?.let {
            val startTime = formatTime(action.startDateTimeUtc)
            val status = mapActionStatusTypeToHumanString(action.status)
            val observation = action.observations?.let { "- $it" } ?: ""
            return "$startTime - $status - début $observation"
        }
    }

    fun formatNavControl(action: ActionControlEntity?): String? {
        return action?.let {
            val startTime = formatTime(action.startDateTimeUtc)
            val endTime = formatTime(action.endDateTimeUtc)
            val vesselIdentifier = action.vesselIdentifier?.let { "- $it" } ?: ""
            return "$startTime / $endTime - Contrôle administratif $vesselIdentifier"
        }
    }
}
