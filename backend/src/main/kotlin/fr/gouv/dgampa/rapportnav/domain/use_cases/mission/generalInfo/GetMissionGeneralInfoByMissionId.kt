package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository

@UseCase
class GetMissionGeneralInfoByMissionId(
    private val infoRepo: IMissionGeneralInfoRepository
) {
    fun execute(missionId: String): MissionGeneralInfoEntity? {
        val info = infoRepo.findByMissionId(missionId = missionId).orElse(null)
        if (info != null) {
            return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(info)
        }
        return null
    }
}
