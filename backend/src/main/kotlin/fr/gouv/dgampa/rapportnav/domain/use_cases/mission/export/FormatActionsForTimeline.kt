package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapActionStatusTypeToHumanString
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.ActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.FishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.NavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import java.time.LocalDate

@UseCase
class FormatActionsForTimeline(
    private val groupActionByDate: GroupActionByDate,
    private val formatDateTime: FormatDateTime,
    private val formatGeoCoords: FormatGeoCoords,
) {

    /**
     * Some characters need to be escaped in a XML compliant manner
     * most notably the following ones:  &, <, >
     */


    fun formatTimeline(actions: List<ActionEntity>?): Map<LocalDate, List<String>>? {

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


    private fun formatAction(action: ActionEntity): String? {
        return when (action) {
            is EnvActionEntity -> formatEnvAction(action)
            is FishActionEntity -> formatFishAction(action)
            is NavActionEntity -> formatNavAction(action)
            else -> null
        }
    }

    private fun formatEnvAction(action: EnvActionEntity): String? {
        return when (action.envActionType) {
            ActionTypeEnum.CONTROL -> formatEnvControl(action)
            ActionTypeEnum.SURVEILLANCE -> formatEnvSurveillance(action)
            else -> null
        }
    }

    private fun formatFishAction(action: FishActionEntity): String? {
        return formatFishControl(action)
    }

    fun formatNavAction(action: NavActionEntity): String? {
        return when (action.actionType) {
            ActionType.STATUS -> formatNavStatus(action)
            ActionType.CONTROL -> formatNavControl(action)
            ActionType.NOTE -> formatNavActionNote(action = action)
            ActionType.ANTI_POLLUTION -> formatNavActionCommon(
                action = action,
                title = "Opération de lutte anti-pollution"
            )

            ActionType.BAAEM_PERMANENCE -> formatNavActionCommon(
                action = action,
                title = "Permanence BAAEM"
            )

            ActionType.ILLEGAL_IMMIGRATION -> formatNavActionCommon(
                action = action,
                title = "Lutte contre l'immigration illégale"
            )

            ActionType.NAUTICAL_EVENT -> formatNavActionCommon(
                action = action,
                title = "Sécu de manifestation nautique"
            )

            ActionType.PUBLIC_ORDER -> formatNavActionCommon(
                action = action,
                title = "Maintien de l'ordre public"
            )

            ActionType.REPRESENTATION -> formatNavActionCommon(
                action = action,
                title = "Représentation"
            )

            ActionType.VIGIMER -> formatNavActionCommon(action = action, title = "Permanence Vigimer")
            ActionType.RESCUE -> formatNavActionCommon(
                action = action,
                title = "Assistance et sauvetage"
            )

            else -> null
        }
    }

    fun formatEnvControl(action: EnvActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val facade = action.facade?.let { " - $it" } ?: ""
            val themes = action.themes?.mapNotNull { it.name }?.joinToString(", ")
            val amountOfControls = action.actionNumberOfControls?.let { " - $it contrôle(s)" } ?: ""
            "$startTime / $endTime - Contrôle Environnement$facade - $themes$amountOfControls"
        }
    }

    fun formatEnvSurveillance(action: EnvActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val themes = action.themes?.mapNotNull { it.name }?.joinToString(", ")
            "$startTime / $endTime - Surveillance Environnement - $themes"
        }
    }

    fun formatFishControl(action: FishActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.actionDatetimeUtc)
            val coords = if (action.fishActionType == MissionActionType.LAND_CONTROL) {
                "à terre - ${action.portName} (${action.portLocode})"
            } else {
                val (lat, lon) = formatGeoCoords.formatLatLon(action.latitude, action.longitude)
                "en mer - (DD): $lat,$lon"
            }

            val vesselInfo = action.vesselName?.let { "$it (${action.externalReferenceNumber})" } ?: ""

            val seizureAndDiversion = if (action.seizureAndDiversion == true) " - retour du navire au port" else ""
            val natinfs: String = listOfNotNull(
                action.fishInfractions.map { it.natinf },
            ).flatten().distinct().let { list ->
                if (list.isEmpty()) {
                    " - RAS"
                } else {
                    " - NATINF: ${list.filterNotNull().joinToString(" + ")}"
                }
            }
            val pvCount = listOfNotNull(
                action.fishInfractions.map { it.infractionType },
            ).flatten().count { it == InfractionType.WITH_RECORD }
            val pv = if (pvCount > 0) "$pvCount PV" else "sans PV"

            "$startTime - Contrôle Pêche - $coords - $vesselInfo - Infractions: $pv$natinfs$seizureAndDiversion"
        }
    }

    fun formatNavStatus(action: NavActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val status = mapActionStatusTypeToHumanString(action.status)
            val reason = action.reason?.let { " - ${it.toString().lowercase().replaceFirstChar { it.uppercase() }}" } ?: ""
            val observation = action.observations?.let { " - $it" } ?: ""
            "$startTime - $status$reason$observation"
        }
    }

    fun formatNavControl(action: NavActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val vesselIdentifier = action.vesselIdentifier?.let { "- $it" } ?: ""
            "$startTime / $endTime - Contrôle administratif $vesselIdentifier"
        }
    }

    private fun formatNavActionCommon(
        action: NavActionEntity,
        title: String?
    ): String? {
        return action.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = action.endDateTimeUtc?.let { " / ${formatDateTime.formatTime(it)}" } ?: ""
            val titleStr = if (!title.isNullOrEmpty()) " - $title" else ""
            val observation =
                if (!action.observations.isNullOrEmpty()) " - ${action.observations}" else ""
            "$startTime$endTime$titleStr$observation"
        }
    }

    private fun formatNavActionNote(action: NavActionEntity): String? {
        return action.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val observation =
                if (!action.observations.isNullOrEmpty()) " - ${action.observations}" else " - aucune observation"
            "$startTime$observation"
        }
    }

}
