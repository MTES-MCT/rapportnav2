package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlVesselAdministrative
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*

@Entity
@Table(name = "control_administrative_vessel")
data class ControlVesselAdministrativeModel(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "control_administrative_vessel_id_seq")
    @SequenceGenerator(name = "control_administrative_vessel_id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false)
    var id: Int,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false, insertable = false, updatable = false)
    var actionControlId: Int,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "compliant_operating_permit", nullable = true)
    var compliantOperatingPermit: Boolean? = null,

    @Column(name = "up_to_date_navigation_permit", nullable = true)
    var upToDateNavigationPermit: Boolean? = null,

    @Column(name = "compliant_security_documents", nullable = true)
    var compliantSecurityDocuments: Boolean? = null,

    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel
) {
    fun toControlVesselAdministrative() = actionControl.id?.let {
        ControlVesselAdministrative(
        id = id,
        missionId = missionId,
        actionControlId = it,
        confirmed = confirmed,
        compliantOperatingPermit = compliantOperatingPermit,
        upToDateNavigationPermit = upToDateNavigationPermit,
        compliantSecurityDocuments = compliantSecurityDocuments
    )
    }
}
