package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity

data class Natinf(
    val infraction: String,
    val natinfCode: Int,
) {
    fun toNatinfEntity(): NatinfEntity {
        return NatinfEntity(
            infraction = infraction,
            natinfCode = natinfCode
        )
    }

    companion object {
        fun fromNatinfEntity(natinf: NatinfEntity) = Natinf(
            infraction = natinf.infraction,
            natinfCode = natinf.natinfCode,
        )
    }
}
