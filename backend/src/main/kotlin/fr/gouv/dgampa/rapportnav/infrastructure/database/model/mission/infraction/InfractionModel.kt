package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.FormalNoticeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.toStringOrNull
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlModel
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "infraction")
class InfractionModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_id", nullable = false)
    var actionId: String,

    @Column(name = "control_type", nullable = false)
    var controlType: String,

    @Column(name = "formal_notice", nullable = true)
    var formalNotice: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @OneToMany(mappedBy = "infraction", targetEntity = InfractionNatinfModel::class)
    var infractions: List<InfractionNatinfModel>? = null,

    @ManyToOne
    @JoinColumn(name = "control_id", referencedColumnName = "id")
    @JsonIgnore
    var control: ControlModel? = null,

    @OneToMany(
        cascade = [CascadeType.ALL],
        fetch = FetchType.LAZY,
        mappedBy = "infraction",
        targetEntity = InfractionEnvTargetModel::class
    )
    @JsonIgnore
    var target: List<InfractionEnvTargetModel>? = null


) {
    fun toInfractionEntity(): InfractionEntity {
        return InfractionEntity(
            id = id,
            missionId = missionId,
            actionId = actionId,
            controlId = control?.id,
            controlType = ControlType.valueOf(controlType),
            formalNotice = formalNotice?.let { FormalNoticeEnum.valueOf(it) },
            observations = observations,
            target = target?.map { it.toInfractionEnvTargetEntity() }?.firstOrNull()
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity) = InfractionModel(
            id = infraction.id,
            missionId = infraction.missionId,
            actionId = infraction.actionId,
            controlType = infraction.controlType.toString(),
            formalNotice = infraction.formalNotice?.toStringOrNull(),
            observations = infraction.observations,
            target = infraction.target?.let { listOf(InfractionEnvTargetModel.fromInfractionEnvTargetEntity(
                infraction.target!!
            )) }
        )
    }
}
