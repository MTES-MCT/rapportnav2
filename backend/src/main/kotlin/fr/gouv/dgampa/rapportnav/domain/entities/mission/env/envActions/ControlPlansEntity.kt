package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanSubThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanTagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanThemeEntity

data class ControlPlansEntity(
    val themes: List<ControlPlanThemeEntity>,
    val subThemes: List<ControlPlanSubThemeEntity>,
    val tags: List<ControlPlanTagEntity>
)
