package fr.gouv.dgampa.rapportnav.infrastructure.rnip.inputs

enum class RnipTypeInterventionInput(val code: Int) {
    PROPRIETAIRE(1),
    AFFRETEUR(2),
    CAPITAINE(3),
    IMPORTATEUR(4),
    ACHETEUR(5),
    ETABLISSEMENT(7),
    REPRESENTANT(8);
}
