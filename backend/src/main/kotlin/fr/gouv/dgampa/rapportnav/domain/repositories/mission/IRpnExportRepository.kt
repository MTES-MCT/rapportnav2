package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity
import java.time.LocalDate
import java.time.ZonedDateTime

data class ExportParams(
    val service: String?,
    val id: String,
    val startDateTime: ZonedDateTime?,
    val endDateTime: ZonedDateTime?,
    val presenceMer: Map<String, Int>,
    val presenceQuai: Map<String, Int>,
    val indisponibilite: Map<String, Int>,
    val nbJoursMer: Int,
    val dureeMission: Int,
    val patrouilleEnv: Int,
    val patrouilleMigrant: Int,
    val distanceMilles: Float?,
    val goMarine: Float?,
    val essence: Float?,
    val crew: List<MissionCrewEntity>,
    val timeline: Map<LocalDate, List<String>>?
)

interface IRpnExportRepository {
    fun exportOdt(params: ExportParams): MissionExportEntity?
}
