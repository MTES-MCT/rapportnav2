package fr.gouv.dgampa.rapportnav.infrastructure.bff.adapters.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlResult
import java.time.ZonedDateTime
import java.util.*

data class ControlAdministrativeInput(
    val id: UUID?,
    val missionId: Int,
    val actionControlId: UUID,
    val confirmed: Boolean?,
    val compliantOperatingPermit: ControlResult?,
    val upToDateNavigationPermit: ControlResult?,
    val compliantSecurityDocuments: ControlResult?,
    val observations: String?,
    val deletedAt: ZonedDateTime? = null,
) {
    fun toControlAdministrativeEntity(): ControlAdministrativeEntity {
        return ControlAdministrativeEntity(
            id = id ?: UUID.randomUUID(),
            missionId = missionId,
            actionControlId = actionControlId,
            confirmed = confirmed,
            compliantOperatingPermit = compliantOperatingPermit,
            upToDateNavigationPermit = upToDateNavigationPermit,
            compliantSecurityDocuments = compliantSecurityDocuments,
            observations = observations,
            deletedAt = deletedAt,
        )
    }
}
