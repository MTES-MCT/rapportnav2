package fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import java.util.*

interface IInquiryRepository {
    fun save(model: InquiryModel): InquiryModel

    fun findById(id: UUID): Optional<InquiryModel>

    fun findByServiceId(serviceId: Int): List<InquiryModel>

    fun deleteById(id: UUID)
}
