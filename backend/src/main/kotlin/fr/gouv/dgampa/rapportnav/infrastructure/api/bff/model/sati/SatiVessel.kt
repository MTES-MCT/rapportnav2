package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.sati

import com.neovisionaries.i18n.CountryCode

data class SatiVessel(
    val id: Int? = null,
    val jpe: SatiJpe? = null,
    val type: String? = null,
    val name: String? = null,
    val immat: String? = null,
    val imo: String? = null,
    val length: Double? = null,
    val extRef: String? = null,
    val ircs: String? = null,
    val owner: SatiParty? = null,
    val flagState: CountryCode? = null,
    val charterer: SatiParty? = null,
    val pnoType: String? = null,
    val tripNumber: String? = null,
    val agent: SatiParty? = null,
    val master: SatiParty? = null,
    val isMasterOwner: Boolean = false
)
