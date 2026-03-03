package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository

@UseCase
class DeleteGeneralInfos(
    private val generalInfosRepository: IMissionGeneralInfoRepository
) {
    fun execute(id: Int? = null) {
        if (id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "DeleteGeneralInfos: cannot delete general info with id $id"
            )
        }
        return generalInfosRepository.deleteById(id)
    }
}
