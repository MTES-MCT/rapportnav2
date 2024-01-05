package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class ControlModel {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    lateinit var id: UUID

    @Column(name = "mission_id", nullable = false)
    var missionId: Int = 0

    @Column(name = "action_control_id", nullable = false, unique = true)
    lateinit var actionControlId: String

    @Column(name = "amount_of_controls", nullable = false)
    var amountOfControls: Int = 1

    @Column(name = "unit_should_confirm", nullable = true)
    var unitShouldConfirm: Boolean? = false

    @Column(name = "unit_has_confirmed", nullable = true)
    var unitHasConfirmed: Boolean? = false

    @Column(name = "observations", nullable = true)
    var observations: String? = null

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        mappedBy = "control",
        targetEntity = InfractionModel::class
    )
    var infractions: List<InfractionModel>? = null
}
