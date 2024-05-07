package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum

data class CompletenessForStatsEntity(
    val status: CompletenessForStatsStatusEnum? = null,
    val sources: List<MissionSourceEnum>? = null
)
