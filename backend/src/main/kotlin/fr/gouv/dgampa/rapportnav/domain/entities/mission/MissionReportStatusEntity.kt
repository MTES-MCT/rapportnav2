package fr.gouv.dgampa.rapportnav.domain.entities.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum

data class MissionReportStatusEntity(
    val status: MissionReportStatusEnum,
    val sources: List<MissionSourceEnum>? = null
)
