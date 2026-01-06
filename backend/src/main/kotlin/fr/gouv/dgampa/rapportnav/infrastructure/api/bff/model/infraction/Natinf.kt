package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity

data class Natinf(
    val infraction: String,
    val natinfCode: Int,
    val regulation: String? = null,
    val infractionCategory: String? = null,
) {
    fun toNatinfEntity(): NatinfEntity {
        return NatinfEntity(
            infraction = infraction,
            natinfCode = natinfCode,
            regulation = regulation,
            infractionCategory = infractionCategory,
        )
    }

    companion object {
        fun fromNatinfEntity(natinf: NatinfEntity) = Natinf(
            infraction = natinf.infraction,
            natinfCode = natinf.natinfCode,
            regulation = natinf.regulation,
            infractionCategory = natinf.infractionCategory,
        )
    }
}
