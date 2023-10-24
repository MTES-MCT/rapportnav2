package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.stringToControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.toStringOrNull
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionControlModel
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "control_administrative")
data class ControlAdministrativeModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_control_id", nullable = false, insertable = false, updatable = false)
    var actionControlId: UUID,

    @Column(name = "confirmed", nullable = true)
    var confirmed: Boolean? = false,

    @Column(name = "compliant_operating_permit", nullable = true)
    var compliantOperatingPermit: String? = null,

    @Column(name = "up_to_date_navigation_permit", nullable = true)
    var upToDateNavigationPermit: String? = null,

    @Column(name = "compliant_security_documents", nullable = true)
    var compliantSecurityDocuments: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,


    @OneToOne
    @JoinColumn(name = "action_control_id", referencedColumnName = "id")
    @JsonIgnore
    var actionControl: ActionControlModel? = null

) {
    fun toControlAdministrative() = ControlAdministrativeEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        confirmed = confirmed,
        compliantOperatingPermit = stringToControlResult(compliantOperatingPermit),
        upToDateNavigationPermit = stringToControlResult(upToDateNavigationPermit),
        compliantSecurityDocuments = stringToControlResult(compliantSecurityDocuments),
        observations = observations,
        deletedAt = deletedAt,
    )

    companion object {
        fun fromControlAdministrative(control: ControlAdministrativeEntity, actionControl: ActionControlModel): ControlAdministrativeModel {
            return ControlAdministrativeModel(
                id = control.id,
                missionId = control.missionId,
                actionControlId = actionControl.id,
                confirmed = control.confirmed,
                compliantOperatingPermit = control.compliantOperatingPermit.toStringOrNull(),
                upToDateNavigationPermit = control.upToDateNavigationPermit.toStringOrNull(),
                compliantSecurityDocuments = control.compliantSecurityDocuments.toStringOrNull(),
                observations = control.observations,
                deletedAt = control.deletedAt,
                actionControl = actionControl
            )
        }
    }

}
