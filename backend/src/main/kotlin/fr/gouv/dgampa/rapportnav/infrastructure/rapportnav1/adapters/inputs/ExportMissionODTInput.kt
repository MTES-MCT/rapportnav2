package fr.gouv.dgampa.rapportnav.infrastructure.rapportnav1.adapters.inputs

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity

data class TimelineActions(
    val date: String,
    val freeNote: List<TimelineActionItem>
)

data class TimelineActionItem(
    val observations: String
)


data class ExportMissionODTInput(
    val service: String?,
    val id: String?,
    val startDateTime: String?,
    val endDateTime: String?,
    val presenceMer: Map<String, Int>,
    val presenceQuai: Map<String, Int>,
    val indisponibilite: Map<String, Int>,
    val nbJoursMer: Int,
    val dureeMission: Int,
    val patrouilleSurveillanceEnvInHours: Float? = null,
    val patrouilleMigrantInHours: Float? = null,
    val distanceMilles: Float? = null,
    val goMarine: Float? = null,
    val essence: Float? = null,
    val crew: List<MissionCrewEntity>,
    val timeline: List<TimelineActions>,
    val rescueInfo: Map<String, String>? = null,
    val nauticalEventsInfo: Map<String, String>? = null,
    val antiPollutionInfo: Map<String, String>? = null,
    val baaemAndVigimerInfo: Map<String, String>? = null,
    val observations: String? = ""
)
