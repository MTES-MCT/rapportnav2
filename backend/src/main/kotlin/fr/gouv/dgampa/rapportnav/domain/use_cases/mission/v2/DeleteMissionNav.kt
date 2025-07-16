package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.MissionSourceEnum
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.DeleteNavAction
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetNavActionListByOwnerId
import java.util.*
import kotlin.jvm.optionals.getOrNull

@UseCase
class DeleteMissionNav(
    private val missionRepo: IMissionNavRepository,
    private val deleteNavAction: DeleteNavAction,
    private val getNavActionListByOwnerId: GetNavActionListByOwnerId
) {
    fun execute(id: UUID? = null, serviceId: Int? = null) {
        if (id == null) return
        val mission = missionRepo.finById(id = id).getOrNull() ?: return
        if (mission.serviceId != serviceId) return
        if (!listOf(MissionSourceEnum.RAPPORT_NAV, MissionSourceEnum.RAPPORTNAV).contains(mission.missionSource)) return
        deleteActions(ownerId = mission.id)
        return missionRepo.deleteById(mission.id)
    }

    private fun deleteActions(ownerId: UUID): List<Unit> {
        return getNavActionListByOwnerId.execute(ownerId = ownerId).map { deleteNavAction.execute(id = it.id) }
    }
}
