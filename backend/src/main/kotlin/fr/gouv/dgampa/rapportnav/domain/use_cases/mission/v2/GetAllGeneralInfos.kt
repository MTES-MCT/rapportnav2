package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel

@UseCase
class GetAllGeneralInfos(
    private val repository: IGeneralInfoRepository,
) {
    fun execute(): List<GeneralInfoModel> {
        return repository.findAll()
    }
}
