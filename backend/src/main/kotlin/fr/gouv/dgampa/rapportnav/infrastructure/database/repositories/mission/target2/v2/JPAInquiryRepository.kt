package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2.IInquiryRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2.IDBInquiryRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Repository
class JPAInquiryRepository(
    private val dbInquiryRepository: IDBInquiryRepository
) : IInquiryRepository {

    override fun findById(id: UUID): Optional<InquiryModel> {
        return dbInquiryRepository.findById(id)
    }

    @Transactional
    override fun save(model:InquiryModel):  InquiryModel {
        return try {
            dbInquiryRepository.save(model)
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

    override fun findByServiceId(serviceId: Int): List<InquiryModel> {
        return dbInquiryRepository.findByServiceId(serviceId)
    }

    override fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<InquiryModel?> {
        return dbInquiryRepository.findAllBetweenDates(
            startBeforeDateTime = startBeforeDateTime,
            endBeforeDateTime = endBeforeDateTime
        )
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbInquiryRepository.deleteById(id)
    }
}
