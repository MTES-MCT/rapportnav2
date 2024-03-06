package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.export

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlEntity
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

    fun formatFishControl(action: MissionAction): String {
        // Code to format MissionActionEntity (Fish)
        return "FishControl"
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
