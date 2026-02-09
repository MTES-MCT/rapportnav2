package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.GeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import java.util.*

@UseCase
class GetMissionGeneralInfoByMissionId(
    private val infoRepo: IGeneralInfoRepository
) {
    fun execute(missionId: Int): GeneralInfoEntity? {
        val info = infoRepo.findByMissionId(missionId = missionId).orElse(null)?: return null
        return GeneralInfoEntity.fromGeneralInfoModel(info)
    }

    fun execute(missionIdUUID: UUID): GeneralInfoEntity? {
        val info = infoRepo.findByMissionIdUUID(missionIdUUID = missionIdUUID).orElse(null)?: return null
        return GeneralInfoEntity.fromGeneralInfoModel(info)
    }
}
