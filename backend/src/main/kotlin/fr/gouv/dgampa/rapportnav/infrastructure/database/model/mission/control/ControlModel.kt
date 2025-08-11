package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@EntityListeners(AuditingEntityListener::class)
abstract class ControlModel {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    open var id: UUID = UUID.randomUUID()

    @Column(name = "mission_id", nullable = false)
    open var missionId: Int = 0

    @Column(name = "action_control_id", nullable = false, unique = true)
    open var actionControlId: String = ""

    @Column(name = "amount_of_controls", nullable = false)
    open var amountOfControls: Int = 1

    @Column(name = "unit_should_confirm", nullable = true)
    open var unitShouldConfirm: Boolean? = false

    @Column(name = "unit_has_confirmed", nullable = true)
    open var unitHasConfirmed: Boolean? = false

    @Column(name = "observations", nullable = true)
    open var observations: String? = null

    @Column(name = "has_been_done", nullable = true)
    open var hasBeenDone: Boolean? = false

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        mappedBy = "control",
        targetEntity = InfractionModel::class
    )
    open var infractions: List<InfractionModel>? = null

    @CreatedDate
    @Column(name = "created_at", nullable = true, updatable = false)
    open var createdAt: Instant? = null

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    open var updatedAt: Instant? = null

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    open var createdBy: Int? = null

    @LastModifiedBy
    @Column(name = "updated_by")
    open var updatedBy: Int? = null
}
