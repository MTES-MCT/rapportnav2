package fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions

data class ControlsSummary(
    val vesselId: Int,
    val numberOfDiversions: Int,
    val numberOfControlsWithSomeGearsSeized: Int,
    val numberOfControlsWithSomeSpeciesSeized: Int,
    val controls: List<MissionAction>,
)
