package fr.gouv.dgampa.rapportnav.domain.entities.mission.sati

import com.neovisionaries.i18n.CountryCode

data class SatiVesselEntity(
    val id: Int? = null,
    val jpe: SatiJpeEntity? = null,
    val type: String? = null,
    val name: String? = null,
    val immat: String? = null,
    val imo: String? = null,
    val length: Double? = null,
    val extRef: String? = null,
    val ircs: String? = null,
    val owner: SatiPartyEntity? = null,
    val flagState: CountryCode? = null,
    val operator: SatiPartyEntity? = null,
    val agent: SatiPartyEntity? = null,
    val master: SatiPartyEntity? = null,
    val isMasterOwner: Boolean = false
)
