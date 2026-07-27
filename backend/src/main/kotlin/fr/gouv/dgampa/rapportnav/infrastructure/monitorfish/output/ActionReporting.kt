package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

class ActionReporting(
    val id: Int? = null,
    val type: ReportingType,
    val title: String? = null,
    val threats: List<MissionActionReportingThreat> = listOf(),
) {
}

data class MissionActionReportingThreat(
    val natinfCode: Int? = null,
    val threat: String? = null,
    val threatCharacterization: String? = null,
)
