package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import java.time.ZonedDateTime
import java.util.*

data class Infraction(
    val id: UUID,
    val missionId: Int,
    val controlType: ControlType,
    val formalNotice: Boolean? = null,
    val natinfs: List<Natinf>? = null,
    val observations: String? = null,
) {
    fun toInfractionEntity(): InfractionEntity {
        return InfractionEntity(
            id = id,
            missionId = missionId,
            controlType = controlType,
            formalNotice = formalNotice,
            natinfs = natinfs?.map { it.toNatinfEntity() },
            observations = observations,
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity) = Infraction(
            id = infraction.id,
            missionId = infraction.missionId,
            controlType = infraction.controlType,
            formalNotice = infraction.formalNotice,
            natinfs = infraction.natinfs?.map{ Natinf.fromNatinfEntity(it)},
            observations = infraction.observations,
        )
    }
}
