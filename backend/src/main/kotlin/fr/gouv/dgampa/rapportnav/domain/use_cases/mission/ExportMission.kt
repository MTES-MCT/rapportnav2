package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId
)
{

    fun exportOdt(
        missionId: Int
    )
    {
        val presenceMer = mapOf(
            "navigationEffective" to 25,
            "mouillage" to 201,
            "total" to 226
        )

        val presenceQuai = mapOf(
            "maintenance" to 40,
            "meteo" to 5,
            "representation" to 7,
            "adminFormation" to 4,
            "autre" to 8,
            "contrPol" to 9,
            "total" to 25
        )

        val indisponibilite = mapOf(
            "technique" to 25,
            "personnel" to 201,
            "total" to 78
        )


        val agentsCrew = agentsCrewByMissionId.execute(missionId = 2)
        println("JE SUIS AU NIVEAU 2")
         exportRepository.exportOdt(
            service = "pam-iris-a",
             id = "NAMO-2024-4",
             startDateTime = "2024-02-12T12:34:56",
             endDateTime = "2024-02-12T12:34:56",
             presenceMer = presenceMer,
             presenceQuai = presenceQuai,
             indisponibilite = indisponibilite,
             nbJoursMer = 4,
             dureeMission = 3,
             patrouilleEnv = 2,
             patrouilleMigrant = 4,
             distanceMilles = 4,
             goMarine = 6,
             essence = 3,
             crew = listOf()
         )
    }
}
