package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.outputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

class RpnExportOdtOutput (
    val service: String?,
    val id: String?,
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
    val crew: List<MissionCrewEntity>
)
