package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.MissionAction
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionControlEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionFreeNoteEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionStatusEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.mapActionStatusTypeToHumanString
import java.time.ZonedDateTime

@UseCase
class FormatActionToString() {

    fun formatTime(dateTime: ZonedDateTime?): String? {
        return dateTime?.let { it.toLocalTime().toString().padStart(5, '0') } ?: "N/A"
    }


    fun formatEnvControl(action: EnvActionControlEntity?): String? {
        return action?.let {
            val startTime = formatTime(action.actionStartDateTimeUtc)
            val endTime = formatTime(action.actionEndDateTimeUtc)
            val facade = action.facade?.let { " - $it" } ?: ""
            val themes = action.themes?.let { " - ${it.map { theme -> theme.theme }.joinToString(" + ")}" } ?: ""
            val amountOfControls = action.actionNumberOfControls?.let { " - $it contrôles" } ?: ""
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
                action.gearInfractions.map { it.natinf.toString() },
                action.logbookInfractions.map { it.natinf.toString() },
                action.speciesInfractions.map { it.natinf.toString() },
                action.otherInfractions.map { it.natinf.toString() }
            ).flatten().distinct().let { list ->
                if (list.isEmpty()) {
                    " - RAS"
                } else {
                    " - NATINF: ${list.joinToString(" + ")}"
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
