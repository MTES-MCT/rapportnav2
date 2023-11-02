package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import java.io.Serializable
import java.util.*

data class InfractionNatinfKey(
    var infractionId: UUID,
    var natinfCode: Int
) : Serializable
