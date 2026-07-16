package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.v2

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import java.util.UUID

@UseCase
class SoftDeleteMission(
    private val repository: IMissionNavRepository
) {
    fun execute(id: UUID? = null) {
        if (id == null) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "SoftDeleteMission: cannot soft-delete mission with id $id"
            )
        }
        return repository.softDeleteById(id)
    }
}
