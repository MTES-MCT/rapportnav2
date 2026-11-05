package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

import fr.gouv.dgampa.rapportnav.domain.entities.mission.fish.fishActions.InfractionCategory
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.NatinfEntity

data class InfractionDataOutput(
    val natinfCode: Int? = null,
    val regulation: String? = null,
    val infractionCategory: String? = null,
    val infraction: String? = null,
) {
    fun toNatinfEntity(): NatinfEntity? {
        if (natinfCode == null || infraction == null) return null
        return NatinfEntity(
            natinfCode = natinfCode,
            infraction = infraction,
            regulation = regulation,
            infractionCategory = infractionCategoryLabel(),
        )
    }

    // MonitorFish sends the enum name (e.g. FISHING), or "null" when its DB label is unknown;
    // other unknown values are passed through as-is
    private fun infractionCategoryLabel(): String? {
        if (infractionCategory == null || infractionCategory == "null") return null
        return InfractionCategory.entries.find { it.name == infractionCategory }?.value ?: infractionCategory
    }
}
