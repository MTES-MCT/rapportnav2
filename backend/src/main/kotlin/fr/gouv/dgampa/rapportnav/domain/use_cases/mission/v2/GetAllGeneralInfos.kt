package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel

@UseCase
class GetAllGeneralInfos(
    private val repository: IMissionGeneralInfoRepository,
) {
    fun execute(): List<MissionGeneralInfoModel> {
        return repository.findAll()
    }
}
