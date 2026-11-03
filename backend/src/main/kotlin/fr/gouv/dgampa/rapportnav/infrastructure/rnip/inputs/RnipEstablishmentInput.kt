package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs


data class RnipEstablishmentInput(
    val misEnCause: Boolean, // mis_en_cause
    val denomination: String? = null, // denomination
    val siret: String? = null, // siret
    val adresse: RnipAdresseInput? = null,
    val proprietaire: RnipPersonneInput? = null, // type PROPRIETAIRE
    val representant: RnipPersonneInput? = null // type REPRESENTANT
)
