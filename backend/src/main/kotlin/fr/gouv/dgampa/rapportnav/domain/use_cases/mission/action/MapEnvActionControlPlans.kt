package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity

@UseCase
class MapEnvActionControlPlans(
    private val getAllControlPlans: GetAllControlPlans,
) {

    fun execute(controlPlans: List<EnvActionControlPlanEntity>?): ControlPlansEntity? {
        return try {
            val allControlPlans: ControlPlansEntity? = getAllControlPlans.execute()

            if (controlPlans.isNullOrEmpty() || allControlPlans == null) {
                return null
            }

            // Filter and merge control plans based on the provided EnvActionControlPlanEntity
            val matchingControlPlans = ControlPlansEntity(
                themes = allControlPlans.themes.filter { theme ->
                    controlPlans.any { envActionControlPlan ->
                        envActionControlPlan.themeId == null || envActionControlPlan.themeId == theme.id
                    }
                },
                subThemes = allControlPlans.subThemes.filter { subTheme ->
                    controlPlans.any { envActionControlPlan ->
                        envActionControlPlan.subThemeIds?.contains(subTheme.id) ?: false
                    }
                },
                tags = allControlPlans.tags.filter { tag ->
                    controlPlans.any { envActionControlPlan ->
                        envActionControlPlan.tagIds?.contains(tag.id) ?: false
                    }
                }
            )

            // Return the matching control plans
            matchingControlPlans
        } catch (ex: Exception) {
            null
        }
    }


}
