package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
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

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        mappedBy = "control",
        targetEntity = InfractionModel::class
    )
    open var infractions: List<InfractionModel>? = null
}
