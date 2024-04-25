package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

data class EnvActionControlPlanEntity(
    val themeId: Int? = null,
    val subThemeIds: List<Int>? = emptyList(),
    val tagIds: List<Int>? = emptyList(),
)
