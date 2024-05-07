package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionSurveillanceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapActionStatusTypeToHumanString
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActionItem
import fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs.TimelineActions
import java.time.LocalDate

@UseCase
class FormatActionsForTimeline(
    private val groupActionByDate: GroupActionByDate,
    private val formatDateTime: FormatDateTime,
    private val formatGeoCoords: FormatGeoCoords,
    private val mapEnvActionControlPlans: MapEnvActionControlPlans,
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
        return if (action.envAction?.controlAction != null) {
            action.envAction.controlAction.action?.let { envControlAction ->
                formatEnvControl(envControlAction)
            }
        } else if (action.envAction?.surveillanceAction != null) {
            action.envAction.surveillanceAction.action.let { envSurveillanceAction ->
                formatEnvSurveillance(envSurveillanceAction)
            }
        } else {
            null
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


    fun formatEnvControl(action: EnvActionControlEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.actionStartDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.actionEndDateTimeUtc)
            val facade = action.facade?.let { " - $it" } ?: ""
            val filteredControlPlans: ControlPlansEntity? = mapEnvActionControlPlans.execute(action.controlPlans)
            val themes = filteredControlPlans?.themes?.mapNotNull { it.theme }?.joinToString(", ")
            val amountOfControls = action.actionNumberOfControls?.let { " - $it contrôle(s)" } ?: ""
            return "$startTime / $endTime - Contrôle Environnement$facade - $themes$amountOfControls"
        }
    }

    fun formatEnvSurveillance(action: EnvActionSurveillanceEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.actionStartDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.actionEndDateTimeUtc)
            val filteredControlPlans: ControlPlansEntity? = mapEnvActionControlPlans.execute(action.controlPlans)
            val themes = filteredControlPlans?.themes?.mapNotNull { it.theme }?.joinToString(", ")
            return "$startTime / $endTime - Surveillance Environnement - $themes"
        }
    }

    fun formatFishControl(action: MissionAction?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.actionDatetimeUtc)
            val coords = formatGeoCoords.formatLatLon(action.latitude, action.longitude).let {
                "(DD): ${it.first},${it.second}"
            }
            val vesselInfo = "${action.vesselName ?: "N/A"} - ${action.portLocode ?: ""} ${action.vesselId}"
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
            val pv = if (pvCount > 0) "$pvCount PV" else "sans PV"

            return "$startTime - Contrôle Pêche - $coords - $vesselInfo - Infractions: $pv$natinfs$seizureAndDiversion"
        }
    }

    fun formatNavNote(action: ActionFreeNoteEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val observation = action.observations ?: ""
            return "$startTime - $observation"
        }
    }


    fun formatNavStatus(action: ActionStatusEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val status = mapActionStatusTypeToHumanString(action.status)
            val observation = action.observations?.let { "- $it" } ?: ""
            return "$startTime - $status $observation"
        }
    }

    fun formatNavControl(action: ActionControlEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val vesselIdentifier = action.vesselIdentifier?.let { "- $it" } ?: ""
            return "$startTime / $endTime - Contrôle administratif $vesselIdentifier"
        }
    }
}
