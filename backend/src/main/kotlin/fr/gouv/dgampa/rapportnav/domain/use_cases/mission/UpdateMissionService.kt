package fr.gouv.dgampa.rapportnav.domain.use_cases.mission

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.ServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.AgentServiceEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IServiceRepository
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.domain.use_cases.mission.crew.GetActiveCrewForService
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.adapters.generalInfo.MissionServiceInput
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import kotlin.jvm.optionals.getOrNull

@UseCase
class UpdateMissionService(
    private val serviceRepo: IServiceRepository,
    private val missionCrewRepo: IMissionCrewRepository,
    private val infoRepo: IMissionGeneralInfoRepository,
    private val getActiveCrewForService: GetActiveCrewForService,
) {
    fun execute(input: MissionServiceInput): ServiceEntity? {

        // get crew on a mission and delete all
        val oldMissionCrews = missionCrewRepo.findByMissionId(input.missionId);
        oldMissionCrews.forEach { missionCrew ->
            missionCrewRepo.deleteById(missionCrew.id!!)
        }

        // get active agents for current service
        val newMissionCrews: List<AgentServiceEntity> = getActiveCrewForService.execute(serviceId = input.serviceId)
        val missionCrew: List<MissionCrewEntity> = newMissionCrews.map{ MissionCrewEntity(missionId = input.missionId, agent = it.agent, role = it.role) }
        missionCrew.forEach {missionCrewRepo.save(it) }

        // save serviceId into generalInformation
        var info = infoRepo.findByMissionId(input.missionId).getOrNull();
        if (info == null) {
            info = MissionGeneralInfoModel(
                missionId = input.missionId
            )
        }
        info.serviceId = input.serviceId;
        infoRepo.save(MissionGeneralInfoEntity.fromMissionGeneralInfoModel(info));
        return serviceRepo.findById(input.serviceId).getOrNull()?.toServiceEntity();
    }
}
