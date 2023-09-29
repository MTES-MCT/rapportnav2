package fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions

data class ThemeEntity(
    val theme: String? = null,
    val subThemes: List<String>? = listOf(),
    val protectedSpecies: List<String>? = listOf(),
)
