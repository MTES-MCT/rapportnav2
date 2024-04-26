package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.outputs.controlPlans

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanTagEntity

data class ControlPlanTagDataOutput(
    val id: Int,
    val tag: String,
    val themeId: Int,
)
