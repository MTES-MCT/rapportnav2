package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.GeneralInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository

@UseCase
class GetMissionGeneralInfoByMissionId(
    private val infoRepo: IMissionGeneralInfoRepository
) {
    fun execute(missionId: Int): MissionGeneralInfoEntity? {
        val info = infoRepo.findByMissionId(missionId = missionId).orElse(null)
        if (info != null) {
            return info.toMissionGeneralInfoEntity()
        }
        return null
    }
}
