package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType

data class SatiEstablishmentEntity(
    val id: Int? = null,
    val type: SectorEstablishmentType? = null,
    val subType: SectorEstablishmentSubType? = null,
    val isNotListed: Boolean = false,
    val isNotSameAddress: Boolean = false,
    val owner: SatiPartyEntity? = null,
    val operator: SatiPartyEntity? = null,
    val address: AddressEntity? = null
)
