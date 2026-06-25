package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import java.time.Instant

data class SatiInspectorEntity(
    val id: Int? = null,
    val agentId: Int? = null,
    val party: SatiPartyEntity? = null,
    val authorityType: AuthorityType? = null,
    val externalAgentId: String? = null,
    val isOutOfUnit: Boolean = false,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
