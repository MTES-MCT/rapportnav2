package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati


data class SatiInspectorEntity(
    val id: Int? = null,
    val agentId: Int? = null,
    val cardId: String? = null,
    val isPrincipal: Boolean = false,
    val isOutOfUnit: Boolean = false,
    val party: SatiPartyEntity? = null,
    val authorityType: AuthorityType? = null
)
