package fr.gouv.dgampa.rapportnav.infrastructure.monitorfish.output

enum class WeightControlMethod(
    val value: String,
) {
    WEIGHING("WEIGHING"),
    CRATE_COUNT("CRATE_COUNT"),
    SAMPLING("SAMPLING"),
    NOT_APPLICABLE("NOT_APPLICABLE"),
}
