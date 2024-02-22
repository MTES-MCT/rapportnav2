package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.ExtendedEnvMissionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.status.ActionStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IRpnExportRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetAgentsCrewByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo.GetMissionGeneralInfoByMissionId
import org.slf4j.LoggerFactory
import java.time.Duration

@UseCase
class ExportMission(
    private val exportRepository: IRpnExportRepository,
    private val getMissionGeneralInfoByMissionId: GetMissionGeneralInfoByMissionId,
    private val agentsCrewByMissionId: GetAgentsCrewByMissionId,
    private val getEnvMissionById: GetEnvMissionById,
    private val getNavMissionById: GetNavMissionById,
    private val getFishActionsByMissionId: GetFishActionsByMissionId
)
{
    private val logger = LoggerFactory.getLogger(ExportMission::class.java)

    fun exportOdt(missionId: Int) {

        val generalInfo: MissionGeneralInfoEntity? = getMissionGeneralInfoByMissionId.execute(missionId)
        val agentsCrew: List<MissionCrewEntity> = agentsCrewByMissionId.execute(missionId = missionId)

    /*    val envMission = getEnvMissionById.execute(missionId = missionId)
        val fishMissionActions = getFishActionsByMissionId.execute(missionId = missionId)
        val navMission = getNavMissionById.execute(missionId = missionId)

        if (envMission === null) {
            logger.error("MissionExport - failed to load mission from MonitorEnv")
            return;
        }

        val mission: Mission = Mission.fromMissionEntity(MissionEntity(envMission, navMission, fishMissionActions))*/


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



        if (generalInfo != null) {
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
                distanceMilles = generalInfo.distanceInNauticalMiles,
                goMarine = generalInfo.consumedGOInLiters,
                essence = generalInfo.consumedFuelInLiters,
                crew = agentsCrew,
            )
        }
    }
}
