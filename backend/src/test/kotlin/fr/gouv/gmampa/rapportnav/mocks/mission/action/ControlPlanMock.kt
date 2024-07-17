package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanSubThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanTagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity

class ControlPlanMock {

    val theme1 = ControlPlanThemeEntity(
        id = 1,
        theme = "AMP sans réglementation particulière"
    )
    val theme2 = ControlPlanThemeEntity(
        id = 2,
        theme = "Activités et manifestations soumises à évaluation d’incidence Natura 2000"
    )
    val theme3 = ControlPlanThemeEntity(
        id = 3,
        theme = "Atteintes aux biens culturels maritimes"
    )

    val subTheme1 = ControlPlanSubThemeEntity(
        id = 1,
        themeId = 1,
        subTheme = "Avitaillement / soutage",
        year = 2024
    )
    val subTheme2 = ControlPlanSubThemeEntity(
        id = 2,
        themeId = 2,
        subTheme = "Arrêté réglementation pêche",
        year = 2024
    )
    val subTheme3 = ControlPlanSubThemeEntity(
        id = 3,
        themeId = 2,
        subTheme = "Autre",
        year = 2024
    )

    val tag1 = ControlPlanTagEntity(id = 1, themeId = 1, tag = "Oiseaux")
    val tag2 = ControlPlanTagEntity(id = 2, themeId = 2, tag = "Habitat")
    val tag3 = ControlPlanTagEntity(id = 3, themeId = 3, tag = "Autres espèces protégées")

    fun createListWithFirst(): ControlPlansEntity {
        return ControlPlansEntity(
            themes = listOf(theme1),
            subThemes = listOf(subTheme1),
            tags = listOf(tag1)
        )
    }

    fun createFullList(): ControlPlansEntity {
        return ControlPlansEntity(
            themes = listOf(theme1, theme2, theme3),
            subThemes = listOf(subTheme1, subTheme2, subTheme3),
            tags = listOf(tag1, tag2, tag3)
        )
    }

}
