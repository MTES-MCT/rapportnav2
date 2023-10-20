package fr.gouv.dgampa.rapportnav.domain.use_cases.missions

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.NavMission
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionControlRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionStatusRepository
import org.slf4j.LoggerFactory

@UseCase
class GetNavMissionById(
    private val navActionControlRepository: INavActionControlRepository,
    private val navStatusRepository: INavActionStatusRepository
    ) {
    private val logger = LoggerFactory.getLogger(GetNavMissionById::class.java)

    fun execute(missionId: Int): NavMission {
        val controls = navActionControlRepository.findAllByMissionId(missionId=missionId).filter { it.deletedAt == null }.map { it.toNavAction() }
        val statuses = navStatusRepository.findAllByMissionId(missionId=missionId).map { it.toNavAction() }
        val actions = controls + statuses
        val mission = NavMission(id = missionId, actions = actions)
        return mission
    }

}
