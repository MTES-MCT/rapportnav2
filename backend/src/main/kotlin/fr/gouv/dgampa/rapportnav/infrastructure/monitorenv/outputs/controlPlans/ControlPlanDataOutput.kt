package fr.gouv.dgampa.rapportnav.infrastructure.monitorenv.outputs.controlPlans

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanSubThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanTagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity

data class ControlPlanDataOutput(
    val themes: Map<Int, ControlPlanThemeDataOutput>,
    val subThemes: Map<Int, ControlPlanSubThemeDataOutput>,
    val tags: Map<Int, ControlPlanTagDataOutput>,
) {
    fun toControlPlansEntity(): ControlPlansEntity {
        val themeEntities = themes.map { (id, themeData) ->
            ControlPlanThemeEntity(id, themeData.theme)
        }
        val subThemeEntities = subThemes.map { (id, subThemeData) ->
            ControlPlanSubThemeEntity(id, subThemeData.themeId, subThemeData.subTheme, subThemeData.year)
        }
        val tagEntities = tags.map { (id, tagData) ->
            ControlPlanTagEntity(id, tagData.tag, tagData.themeId)
        }
        return ControlPlansEntity(themeEntities, subThemeEntities, tagEntities)
    }
}
