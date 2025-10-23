package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.v2.InquiryModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface IDBInquiryRepository : JpaRepository<InquiryModel, UUID> {

    override fun findById(id: UUID): Optional< InquiryModel>

    fun findByServiceId(serviceId: Int): List<InquiryModel>

    @Query("""
    SELECT i FROM InquiryModel i
    WHERE (i.startDateTimeUtc >= :startBeforeDateTime AND i.startDateTimeUtc <= :endBeforeDateTime)
    ORDER BY i.startDateTimeUtc DESC
    """)
    fun findAllBetweenDates(
        @Param("startBeforeDateTime") startBeforeDateTime: Instant,
        @Param("endBeforeDateTime") endBeforeDateTime: Instant
    ): List<InquiryModel?>
}
