package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.stringToControlResult
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.toStringOrNull
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
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

    @Column(name = "action_control_id", nullable = false)
    var actionControlId: String,

    @Column(name = "amount_of_controls", nullable = false)
    var amountOfControls: Int = 1,

    @Column(name = "unit_should_confirm", nullable = true)
    var unitShouldConfirm: Boolean? = false,

    @Column(name = "unit_has_confirmed", nullable = true)
    var unitHasConfirmed: Boolean? = false,

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

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, mappedBy = "controlAdministrative", targetEntity = InfractionModel::class)
    var infractions: List<InfractionModel>? = null

) {
    fun toControlAdministrativeEntity() = ControlAdministrativeEntity(
        id = id,
        missionId = missionId,
        actionControlId = actionControlId,
        amountOfControls = amountOfControls,
        unitHasConfirmed = unitHasConfirmed,
        unitShouldConfirm = unitShouldConfirm,
        compliantOperatingPermit = stringToControlResult(compliantOperatingPermit),
        upToDateNavigationPermit = stringToControlResult(upToDateNavigationPermit),
        compliantSecurityDocuments = stringToControlResult(compliantSecurityDocuments),
        observations = observations,
        deletedAt = deletedAt,
        infractions = infractions?.map { it.toInfractionEntity() }
    )

    companion object {
        fun fromControlAdministrativeEntity(control: ControlAdministrativeEntity): ControlAdministrativeModel {
            return ControlAdministrativeModel(
                id = control.id,
                missionId = control.missionId,
                actionControlId = control.actionControlId,
                amountOfControls = control.amountOfControls,
                unitHasConfirmed = control.unitHasConfirmed,
                unitShouldConfirm = control.unitShouldConfirm,
                compliantOperatingPermit = control.compliantOperatingPermit.toStringOrNull(),
                upToDateNavigationPermit = control.upToDateNavigationPermit.toStringOrNull(),
                compliantSecurityDocuments = control.compliantSecurityDocuments.toStringOrNull(),
                observations = control.observations,
                deletedAt = control.deletedAt,
                infractions = control.infractions?.map{InfractionModel.fromInfractionEntity(it) } ,
            )
        }
    }

}
