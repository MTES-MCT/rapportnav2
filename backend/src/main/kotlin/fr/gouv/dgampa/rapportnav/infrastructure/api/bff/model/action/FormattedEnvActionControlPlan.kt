package fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity

data class FormattedEnvActionControlPlan(
    val theme: String,
    val subThemes: List<String>? = null,
    val tags: List<String>? = null,
) {

    companion object {
        fun fromControlPlansEntity(data: ControlPlansEntity?): List<FormattedEnvActionControlPlan>? {
            return data?.themes?.map { theme ->
                FormattedEnvActionControlPlan(
                    theme = theme.theme,
                    subThemes = data.subThemes.filter { it.themeId == theme.id }.mapNotNull { it.subTheme },
                    tags = data.tags.filter { it.themeId == theme.id }.mapNotNull { it.tag },
                )
            }
        }
    }

}
