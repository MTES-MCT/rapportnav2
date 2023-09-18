package fr.gouv.dgampa.rapportnav.infrastructure.database.model.control

import fr.gouv.dgampa.rapportnav.domain.entities.control.ControlVesselAdministrative
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "control_administrative_vessel")
data class ControlVesselAdministrativeModel(
    @Id
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID = UUID.randomUUID(),

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "compliant_operating_permit", nullable = true)
    var compliantOperatingPermit: Boolean? = null,

    @Column(name = "up_to_date_navigation_permit", nullable = true)
    var upToDateNavigationPermit: Boolean? = null,

    @Column(name = "compliant_security_documents", nullable = true)
    var compliantSecurityDocuments: Boolean? = null,
) {
    fun toControlVesselAdministrative() = ControlVesselAdministrative(
        id = id,
        missionId = missionId,
        confirmed = confirmed,
        compliantOperatingPermit = compliantOperatingPermit,
        upToDateNavigationPermit = upToDateNavigationPermit,
        compliantSecurityDocuments = compliantSecurityDocuments
    )
}