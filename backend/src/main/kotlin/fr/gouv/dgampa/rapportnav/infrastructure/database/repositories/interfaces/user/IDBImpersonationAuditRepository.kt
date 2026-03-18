package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.user

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.user.ImpersonationAuditModel
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface IDBImpersonationAuditRepository : JpaRepository<ImpersonationAuditModel, Int> {
    fun findByAdminUserIdAndTimestampAfter(adminUserId: Int, after: Instant): List<ImpersonationAuditModel>
    fun findByTargetServiceIdAndTimestampAfter(serviceId: Int, after: Instant): List<ImpersonationAuditModel>
}
