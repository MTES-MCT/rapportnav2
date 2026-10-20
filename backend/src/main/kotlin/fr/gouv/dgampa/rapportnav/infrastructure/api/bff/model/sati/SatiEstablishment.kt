package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType

data class SatiEstablishment(
    val id: Int? = null,
    val type: SectorEstablishmentType? = null,
    val subType: SectorEstablishmentSubType? = null,
    val isNotListed: Boolean = false,
    val isNotSameAddress: Boolean = false,
    val owner: SatiParty? = null,
    val operator: SatiParty? = null,
    val address: Address? = null
)
