package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.AuthenticationAuditModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface IDBAuthenticationAuditRepository : JpaRepository<AuthenticationAuditModel, Int> {
    fun findAllByOrderByTimestampDesc(pageable: Pageable): Page<AuthenticationAuditModel>

    fun countByEmailAndSuccessFalseAndTimestampAfter(email: String, after: Instant): Long

    fun countByIpAddressAndSuccessFalseAndTimestampAfter(ipAddress: String, after: Instant): Long
}