package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Infraction
import java.time.ZonedDateTime
import java.util.*

data class InfractionInput(
    val id: String? = null,
    val missionId: Int,
    val controlId: String,
    val controlType: String,
    val formalNotice: Boolean? = null,
    val natinfs: List<NatinfInput>? = null,
    val observations: String? = null,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toInfraction(): Infraction {
        return Infraction(
            id = if (id != null)  UUID.fromString(id) else UUID.randomUUID(),
            missionId = missionId,
            controlType = ControlType.valueOf(controlType),
            natinfs = natinfs?.map { it.toNatinf() },
            formalNotice = formalNotice,
            observations = observations,
            deletedAt = deletedAt,
        )
    }
}
