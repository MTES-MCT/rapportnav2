package fr.gouv.gmampa.rapportnav.mocks.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.EnvActionControlPlanEntity

class EnvActionControlPlanMock {


    val controlPlan1 = EnvActionControlPlanEntity(
        themeId = 1,
        subThemeIds = listOf(1),
        tagIds = listOf(1),
    )

    val controlPlan2 = EnvActionControlPlanEntity(
        themeId = 2,
        subThemeIds = listOf(2, 3),
        tagIds = listOf(2, 3),
    )

    val controlPlan3 = EnvActionControlPlanEntity(
        themeId = 3,
        subThemeIds = listOf(2, 3),
        tagIds = listOf(2),
    )

    fun createList(): List<EnvActionControlPlanEntity> {
        return listOf(controlPlan1, controlPlan2, controlPlan3)

    }

}
