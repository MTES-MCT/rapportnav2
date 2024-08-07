package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.action

import java.time.ZonedDateTime

data class ActionEnvInput(
    val missionId: Int,
    val actionId: String,
    val observationsByUnit: String? = null,
    val startDateTimeUtc: ZonedDateTime? = null,
    val endDateTimeUtc: ZonedDateTime? = null,
)
