package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.output.controlPlans

data class ControlPlanSubThemeDataOutput(
    val id: Int,
    val themeId: Int,
    val subTheme: String,
    val year: Int,
)
