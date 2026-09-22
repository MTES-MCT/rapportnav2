package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.ComputeSati
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetFishActionListByMissionId
import java.util.UUID

@UseCase
class GetSatiById(
    private val satiRepo: ISatiRepository,
    private val getFishActionListByMissionId: GetFishActionListByMissionId,
    private val computeSati: ComputeSati,
) {
    fun execute(id: UUID): SatiEntity? {
        val sati = satiRepo.findById(id) ?: return null
        val missionId = sati.missionId
            ?: throw BackendInternalException(
                message = "GetSatiById: sati=${sati.id} has no missionId, cannot locate its FishAction"
            )

        val action = getFishActionListByMissionId.execute(missionId)
            .find { it.id == sati.actionId.toIntOrNull() }
            ?: throw BackendInternalException(
                message = "GetSatiById: no FishAction id=${sati.actionId} found in mission=$missionId for sati=${sati.id}"
            )

        return computeSati.execute(sati = sati, action = action)
    }
}
