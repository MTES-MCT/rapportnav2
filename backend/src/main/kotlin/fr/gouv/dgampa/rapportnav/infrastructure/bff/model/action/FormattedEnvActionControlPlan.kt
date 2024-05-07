package fr.gouv.dgampa.rapportnav.infrastructure.bff.model.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity

data class FormattedEnvActionControlPlan(
    val themes: List<String>?,
    val subThemes: List<String>?,
    val tags: List<String>?
) {

    companion object {
        fun fromControlPlansEntity(data: ControlPlansEntity?): FormattedEnvActionControlPlan? {
            return if (data != null) {
                FormattedEnvActionControlPlan(
                    themes = data.themes.map { it.theme },
                    subThemes = data.subThemes.map { it.subTheme },
                    tags = data.tags.map { it.tag },
                )
            } else {
                null
            }
        }
    }

}
