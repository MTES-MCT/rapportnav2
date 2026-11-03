package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

import java.time.LocalDate

data class RnipPersonneInput(
    val type: RnipTypeInterventionInput, // type_intervention
    val misEnCause: Boolean, // mis_en_cause
    val nom: String? = null, // nom
    val prenom: String? = null, // prenom
    val dateNaissance: LocalDate? = null, // date_naissance, captain only
    val nationalite: String? = null, // nationalite
    val denomination: String? = null, // denomination
    val siret: String? = null, // siret
    val rcs: String? = null, // rcs, never filled
    val siege: String? = null, // siege, never filled
    val forme: String? = null, // forme, never filled
    val adresse: RnipAdresseInput? = null
)
