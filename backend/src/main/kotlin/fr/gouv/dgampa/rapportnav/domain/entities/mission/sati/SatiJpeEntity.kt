package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.LogbookMessagePurpose
import java.time.Instant

data class SatiJpeEntity(
    val pnoId: String? = null,
    val portId: String? = null,
    val portName: String? = null,
    val tripNumber: String? = null,
    val lastStopDate: Instant? = null,
    val pnoType: LogbookMessagePurpose? = null
)
