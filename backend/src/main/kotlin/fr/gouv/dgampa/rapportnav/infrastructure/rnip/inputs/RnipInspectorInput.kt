package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

data class RnipInspectorInput(
    val nom: String, // nom_de_l_inspecteur
    val prenom: String, // prenom_de_l_inspecteur
    val nomDuService: String, // nom_du_service
    val codeUnite: String, // code_de_l_unite
    val codesEngin: List<String> = emptyList(), // code_engin
    val provenance: String // PROV_NO
)
