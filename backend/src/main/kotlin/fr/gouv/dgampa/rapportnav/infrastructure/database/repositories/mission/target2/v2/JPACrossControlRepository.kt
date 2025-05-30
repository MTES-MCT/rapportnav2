package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.ICrossControlRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.CrossControlModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBCrossControlRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPACrossControlRepository(
    private val dbCrossControlRepository: IDBCrossControlRepository
) : ICrossControlRepository {

    override fun findById(id: UUID): Optional<CrossControlModel> {
        return dbCrossControlRepository.findById(id)
    }

    @Transactional
    override fun save(model:CrossControlModel):  CrossControlModel {
        return try {
            dbCrossControlRepository.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ='${model.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving",
                originalException = e
            )
        }
    }

    override fun findByServiceId(serviceId: Int): List<CrossControlModel> {
        return dbCrossControlRepository.findByServiceId(serviceId)
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbCrossControlRepository.deleteById(id)
    }
}
