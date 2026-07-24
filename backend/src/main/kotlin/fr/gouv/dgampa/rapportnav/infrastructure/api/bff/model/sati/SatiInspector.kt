package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.AuthorityType

data class SatiInspector(
    val id: Int? = null,
    val cardId: String? = null,
    val party: SatiParty? = null,
    val authorityType: AuthorityType? = null,
    val agentId: Int? = null,
    val isOutOfUnit: Boolean = false
)
