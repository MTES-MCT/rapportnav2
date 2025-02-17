package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo.MissionServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import kotlin.jvm.optionals.getOrNull

@UseCase
class UpdateMissionService(
    private val serviceRepo: IServiceRepository,
    private val missionCrewRepo: IMissionCrewRepository,
    private val infoRepo: IMissionGeneralInfoRepository,
    private val agentServiceRepo: IAgentServiceRepository,
) {
    fun execute(input: MissionServiceInput): ServiceEntity? {

        // get crew on a mission and delete all
        val oldMissionCrews = missionCrewRepo.findByMissionId(input.missionId);
        oldMissionCrews.forEach { missionCrew ->
            missionCrewRepo.deleteById(missionCrew.id!!)
        }

        // get agent on a service
        val newMissionCrews = agentServiceRepo.findByServiceId(input.serviceId)
            .map { agent -> agent.toMissionCrewModel(input.missionId) };
        newMissionCrews.forEach { missionCrew -> missionCrewRepo.save(missionCrew!!.toMissionCrewEntity()) }

        // save serviceId into generalInformation
        var info = infoRepo.findByMissionId(input.missionId).getOrNull();
        if (info == null) {
            info = MissionGeneralInfoModel(
                id = input.missionId,
                missionId = input.missionId
            )
        }
        info.serviceId = input.serviceId;
        infoRepo.save(MissionGeneralInfoEntity.fromMissionGeneralInfoModel(info));
        return serviceRepo.findById(input.serviceId).getOrNull()?.toServiceEntity();
    }
}
