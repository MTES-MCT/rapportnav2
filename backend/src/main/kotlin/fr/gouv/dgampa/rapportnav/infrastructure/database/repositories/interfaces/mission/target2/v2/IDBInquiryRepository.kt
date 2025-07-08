package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBInquiryRepository : JpaRepository<InquiryModel, UUID> {

    override fun findById(id: UUID): Optional< InquiryModel>

    fun findByServiceId(serviceId: Int): List<InquiryModel>
}
