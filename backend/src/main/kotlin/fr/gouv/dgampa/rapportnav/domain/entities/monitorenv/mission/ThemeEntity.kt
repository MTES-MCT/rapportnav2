package fr.gouv.dgampa.rapportnav.domain.entities.monitorenv.mission

data class ThemeEntity(
    val theme: String? = null,
    val subThemes: List<String>? = listOf(),
    val protectedSpecies: List<String>? = listOf(),
)
