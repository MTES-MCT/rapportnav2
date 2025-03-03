package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IAgentServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo.MissionServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import kotlin.collections.forEach
import kotlin.jvm.optionals.getOrNull

@UseCase
class UpdateMissionService2(
    private val serviceRepo: IServiceRepository,
    private val missionCrewRepo: IMissionCrewRepository,
    private val infoRepo: IMissionGeneralInfoRepository,
    private val getActiveCrewForService: GetActiveCrewForService,
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

        // get active agents for current service
        val newMissionCrews: List<AgentServiceEntity> = getActiveCrewForService.execute(serviceId = serviceId)
        val missionCrew: List<MissionCrewEntity> = newMissionCrews.map{ MissionCrewEntity(missionId = missionId, agent = it.agent, role = it.role) }
        missionCrew.forEach {missionCrewRepo.save(it) }

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
