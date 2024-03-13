package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.export.MissionExportEntity

data class ExportParams(
    val service: String?,
    val id: String,
    val startDateTime: String?,
    val endDateTime: String?,
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
    val timeline: Map<String, List<String>>?
)

interface IRpnExportRepository {
    fun exportOdt(params: ExportParams): MissionExportEntity?
}
