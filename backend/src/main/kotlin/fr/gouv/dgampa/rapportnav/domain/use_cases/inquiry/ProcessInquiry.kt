package fr.gouv.dgampa.rapportnav.domain.use_cases.inquiry

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2.GetComputeNavActionListByMissionId
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels

@UseCase
class ProcessInquiry(
    private val getVessels: GetVessels,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId
) {
    fun execute(entity: InquiryEntity): InquiryEntity {
        val vessel = getVessels.execute().find {vessel -> vessel.vesselId == entity.vessel?.vesselId   }
        val actions = entity.id?.let { getComputeNavActionListByMissionId.execute(ownerId = it) } ?: emptyList()

        return entity
            .withVessel(vessel)
            .withActions(actions)
    }
}

