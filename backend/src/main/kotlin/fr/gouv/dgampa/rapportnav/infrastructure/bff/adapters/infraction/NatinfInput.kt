package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.infraction

import fr.gouv.dgampa.rapportnav.infrastructure.bff.model.infraction.Natinf

data class NatinfInput(
    val infraction: String,
    val natinfCode: Int,
) {
    fun toNatinf(): Natinf {
        return Natinf(
            infraction = infraction,
            natinfCode = natinfCode,
        )
    }
}
