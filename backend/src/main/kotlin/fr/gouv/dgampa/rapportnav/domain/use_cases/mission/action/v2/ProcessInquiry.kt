package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.action.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InquiryEntity
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2.GetVessels

@UseCase
class ProcessInquiry(
    private val getVessels: GetVessels,
    private val getComputeNavActionListByMissionId: GetComputeNavActionListByMissionId
) {
    fun execute(entity: InquiryEntity): InquiryEntity {
        val vessel = getVessels.execute().find {vessel -> vessel.vesselId == entity.vesselId   }
        val actions = getComputeNavActionListByMissionId.execute(ownerId = entity.missionIdUUID)

        return entity
            .withVessel(vessel)
            .withActions(actions)
    }
}

