package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

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
class UpdateMissionService2(
    private val serviceRepo: IServiceRepository,
    private val missionCrewRepo: IMissionCrewRepository,
    private val infoRepo: IMissionGeneralInfoRepository,
    private val agentServiceRepo: IAgentServiceRepository,
) {
    fun execute(
        serviceId: Int,
        missionId: Int
    ): ServiceEntity? {

        // get crew on a mission and delete all
        val oldMissionCrews = missionCrewRepo.findByMissionId(missionId);
        oldMissionCrews.forEach { missionCrew ->
            missionCrewRepo.deleteById(missionCrew.id!!)
        }

        // get agent on a service
        val newMissionCrews = agentServiceRepo.findByServiceId(serviceId)
            .map { agent -> agent.toMissionCrewModel(missionId) };
        newMissionCrews.forEach { missionCrew -> missionCrewRepo.save(missionCrew!!.toMissionCrewEntity()) }

        // save serviceId into generalInformation
        var info = infoRepo.findByMissionId(missionId).getOrNull();
        if (info == null) {
            info = MissionGeneralInfoModel(
                id = missionId,
                missionId = missionId
            )
        }
        info.serviceId = serviceId;
        infoRepo.save(MissionGeneralInfoEntity.Companion.fromMissionGeneralInfoModel(info));
        return serviceRepo.findById(serviceId).getOrNull()?.toServiceEntity();
    }
}
