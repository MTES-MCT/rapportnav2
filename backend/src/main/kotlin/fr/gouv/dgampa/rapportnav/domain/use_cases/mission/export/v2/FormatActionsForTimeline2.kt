package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ActionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapActionStatusTypeToHumanString
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionEnvActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionFishActionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavActionEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.GroupActionByDate2
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.MapEnvActionControlPlans
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatDateTime
import fr.gouv.dgampa.rapportnav.domain.use_cases.utils.FormatGeoCoords
import java.time.LocalDate

@UseCase
class FormatActionsForTimeline2(
    private val groupActionByDate: GroupActionByDate2,
    private val formatDateTime: FormatDateTime,
    private val formatGeoCoords: FormatGeoCoords,
    private val mapEnvActionControlPlans: MapEnvActionControlPlans,
) {

    /**
     * Some characters need to be escaped in a XML compliant manner
     * most notably the following ones:  &, <, >
     */


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


    private fun formatAction(action: MissionActionEntity): String? {
        return when (action) {
            is MissionEnvActionEntity -> formatEnvAction(action)
            is MissionFishActionEntity -> formatFishAction(action)
            is MissionNavActionEntity -> formatNavAction(action)
            else -> null
        }
    }

    private fun formatEnvAction(action: MissionEnvActionEntity): String? {
        return when (action.envActionType) {
            ActionTypeEnum.CONTROL -> formatEnvControl(action)
            ActionTypeEnum.SURVEILLANCE -> formatEnvSurveillance(action)
            else -> null
        }
    }

    private fun formatFishAction(action: MissionFishActionEntity): String? {
        return formatFishControl(action)
    }

    fun formatNavAction(action: MissionNavActionEntity): String? {
        return when (action.actionType) {
            ActionType.STATUS -> formatNavStatus(action)
            ActionType.CONTROL -> formatNavControl(action)
            ActionType.NOTE -> formatNavActionCommon(action = action, title = null)
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

    fun formatEnvControl(action: MissionEnvActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val facade = action.facade?.let { " - $it" } ?: ""
            val filteredControlPlans: ControlPlansEntity? = mapEnvActionControlPlans.execute(action.controlPlans)
            val themes = filteredControlPlans?.themes?.mapNotNull { it.theme }?.joinToString(", ")
            val amountOfControls = action.actionNumberOfControls?.let { " - $it contrôle(s)" } ?: ""
            return "$startTime / $endTime - Contrôle Environnement$facade - $themes$amountOfControls"
        }
    }

    fun formatEnvSurveillance(action: MissionEnvActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val filteredControlPlans: ControlPlansEntity? = mapEnvActionControlPlans.execute(action.controlPlans)
            val themes = filteredControlPlans?.themes?.mapNotNull { it.theme }?.joinToString(", ")
            return "$startTime / $endTime - Surveillance Environnement - $themes"
        }
    }

    fun formatFishControl(action: MissionFishActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.actionDatetimeUtc)
            val coords = formatGeoCoords.formatLatLon(action.latitude, action.longitude).let {
                "(DD): ${it.first},${it.second}"
            }
            val vesselInfo = "${action.vesselName ?: "N/A"} - ${action.portLocode ?: ""} ${action.vesselId}"
            val seizureAndDiversion = if (action.seizureAndDiversion == true) " - retour du navire au port" else ""
            val natinfs: String = listOfNotNull(
                action.gearInfractions?.map { it.natinf },
                action.logbookInfractions?.map { it.natinf },
                action.speciesInfractions?.map { it.natinf },
                action.otherInfractions?.map { it.natinf }
            ).flatten().distinct().let { list ->
                if (list.isEmpty()) {
                    " - RAS"
                } else {
                    " - NATINF: ${list.filterNotNull().joinToString(" + ")}"
                }
            }
            val pvCount = listOfNotNull(
                action.gearInfractions?.map { it.infractionType },
                action.logbookInfractions?.map { it.infractionType },
                action.speciesInfractions?.map { it.infractionType },
                action.otherInfractions?.map { it.infractionType }
            ).flatten().count { it == InfractionType.WITH_RECORD }
            val pv = if (pvCount > 0) "$pvCount PV" else "sans PV"

            return "$startTime - Contrôle Pêche - $coords - $vesselInfo - Infractions: $pv$natinfs$seizureAndDiversion"
        }
    }

    fun formatNavStatus(action: MissionNavActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val status = mapActionStatusTypeToHumanString(action.status)
            val observation = action.observations?.let { "- $it" } ?: ""
            return "$startTime - $status $observation"
        }
    }

    fun formatNavControl(action: MissionNavActionEntity?): String? {
        return action?.let {
            val startTime = formatDateTime.formatTime(action.startDateTimeUtc)
            val endTime = formatDateTime.formatTime(action.endDateTimeUtc)
            val vesselIdentifier = action.vesselIdentifier?.let { "- $it" } ?: ""
            return "$startTime / $endTime - Contrôle administratif $vesselIdentifier"
        }
    }

    private fun formatNavActionCommon(
        action: MissionNavActionEntity,
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

}
