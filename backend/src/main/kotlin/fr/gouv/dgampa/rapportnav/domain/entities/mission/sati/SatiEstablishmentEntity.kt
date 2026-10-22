package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.EstablishmentEntity

data class SatiEstablishmentEntity(
    val id: Int? = null,
    val type: SectorEstablishmentType? = null,
    val subType: SectorEstablishmentSubType? = null,
    val owner: SatiPartyEntity? = null,
    val operator: SatiPartyEntity? = null,
    val data: EstablishmentEntity? = null
)
