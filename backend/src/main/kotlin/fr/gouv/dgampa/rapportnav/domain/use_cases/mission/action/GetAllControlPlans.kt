package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanSubThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanTagEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.controlPlan.ControlPlanThemeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.ControlPlansEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IEnvMissionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@UseCase
class GetAllControlPlans(
    private val envAPI: IEnvMissionRepository,
) {

    private val logger: Logger = LoggerFactory.getLogger(GetAllControlPlans::class.java)


    private fun getFakeEnvActionControlPlanEntity(): ControlPlansEntity {
        return ControlPlansEntity(
            themes = listOf(
                ControlPlanThemeEntity(id = 1, theme = "Rejets illicites"),
                ControlPlanThemeEntity(
                    id = 2,
                    theme = "Activités et manifestations soumises à évaluation d’incidence Natura 2000"
                ),
                ControlPlanThemeEntity(id = 3, theme = "Atteintes aux biens culturels maritimes"),
            ),
            subThemes = listOf(
                ControlPlanSubThemeEntity(
                    id = 1,
                    themeId = 1,
                    subTheme = "Avitaillement / soutage",
                    year = 2024
                ),
                ControlPlanSubThemeEntity(
                    id = 2,
                    themeId = 2,
                    subTheme = "Arrêté réglementation pêche",
                    year = 2024
                ),
                ControlPlanSubThemeEntity(
                    id = 3,
                    themeId = 3,
                    subTheme = "Autre",
                    year = 2024
                )
            ),
            tags = listOf(ControlPlanTagEntity(id = 1, themeId = 1, tag = "tag"))
        )
    }

    fun execute(): ControlPlansEntity? {
        try {
            val response =
                envAPI.findAllControlPlans() ?: throw Exception("EnvControlPlans endpoint should not answer null")
            return response
        } catch (ex: Exception) {
            logger.info(ex.message)
            return null
//            return this.getFakeEnvActionControlPlanEntity()
        }

    }
}
