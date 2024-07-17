package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.outputs.controlPlans

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanSubThemeEntity

data class ControlPlanSubThemeDataOutput(
    val id: Int,
    val themeId: Int,
    val subTheme: String,
    val year: Int,
)
