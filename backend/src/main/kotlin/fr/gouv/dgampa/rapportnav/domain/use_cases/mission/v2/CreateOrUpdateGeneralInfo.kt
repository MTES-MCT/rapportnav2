package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionGeneralInfoEntity2
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.api.bff.model.v2.generalInfo.MissionGeneralInfo2

@UseCase
class CreateOrUpdateGeneralInfo(private val repository: IMissionGeneralInfoRepository) {

    fun execute(generalInfo2: MissionGeneralInfo2): MissionGeneralInfoEntity2 {

        val generalInfoModel = repository.save(generalInfo2.toMissionGeneralInfoEntity())

        return MissionGeneralInfoEntity2(
            data = generalInfoModel.toMissionGeneralInfoEntity()
        )

    }
}
