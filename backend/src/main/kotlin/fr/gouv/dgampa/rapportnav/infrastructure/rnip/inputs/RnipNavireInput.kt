package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

data class RnipNavireInput(
    val pavillon: String? = null, // pavillon
    val immatriculationExterne: String? = null, // immatriculation_externe
    val cfr: String, // cfr
    val nom: String? = null, // nom
    val capitaine: RnipPersonneInput? = null, // type CAPITAINE
    val affreteur: RnipPersonneInput? = null, // type AFFRETEUR
    val proprietaire: RnipPersonneInput? = null // type PROPRIETAIRE, when no affreteur
)
