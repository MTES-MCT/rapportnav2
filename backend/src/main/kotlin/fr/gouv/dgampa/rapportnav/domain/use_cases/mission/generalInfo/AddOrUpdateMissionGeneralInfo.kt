package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository

@UseCase
class AddOrUpdateMissionGeneralInfo(
    private val infoRepo: IMissionGeneralInfoRepository
) {
    fun execute(info: MissionGeneralInfoEntity): MissionGeneralInfoEntity {
        val savedData = MissionGeneralInfoEntity.fromMissionGeneralInfoModel(infoRepo.save(info))
        return savedData
    }
}
