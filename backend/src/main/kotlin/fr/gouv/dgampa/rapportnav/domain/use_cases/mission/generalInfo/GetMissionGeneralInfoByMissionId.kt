package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import java.util.*

@UseCase
class GetMissionGeneralInfoByMissionId(
    private val infoRepo: IMissionGeneralInfoRepository
) {
    fun execute(missionId: Int): MissionGeneralInfoEntity? {
        val info = infoRepo.findByMissionId(missionId = missionId).orElse(null)
        if (info != null) {
            return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(info)
        }
        return null
    }

    fun execute(missionIdUUID: UUID): MissionGeneralInfoEntity? {
        val info = infoRepo.findByMissionIdUUID(missionIdUUID = missionIdUUID).orElse(null)
        if (info != null) {
            return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(info)
        }
        return null
    }
}
