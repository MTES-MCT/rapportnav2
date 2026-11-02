package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentSubType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.SectorEstablishmentType
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.Establishment

data class SatiEstablishment(
    val id: Int? = null,
    val type: SectorEstablishmentType? = null,
    val subType: SectorEstablishmentSubType? = null,
    val owner: SatiParty? = null,
    val operator: SatiParty? = null,
    val data: Establishment? = null
)
