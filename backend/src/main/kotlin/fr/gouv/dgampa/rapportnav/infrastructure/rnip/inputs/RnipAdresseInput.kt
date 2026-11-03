package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

data class RnipAdresseInput(
    val codePays: String? = null, // code_pays
    val adresse4: String? = null, // adresse_4, never filled
    val adresse5: String? = null, // adresse_5
    val adresse6: String? = null, // adresse_6
    val codePostal: String? = null // code_postal
)
