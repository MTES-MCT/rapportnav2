package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
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
    var missionId: String,

    @Column(name = "action_id", nullable = false)
    var actionId: String,

    @Column(name = "control_type", nullable = false)
    var controlType: String,

    @Column(name = "infraction_type", nullable = true)
    var infractionType: String? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "infraction_natinf",
        joinColumns = [JoinColumn(name = "infraction_id")]
    )
    @Column(name = "natinf_code")
    var natinfs: List<String> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
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
    var target: List<InfractionEnvTargetModel> = mutableListOf()


) {
    fun toInfractionEntity(): InfractionEntity {
        return InfractionEntity(
            id = id,
            missionId = missionId,
            actionId = actionId,
            controlId = control?.id,
            controlType = ControlType.valueOf(controlType),
            infractionType = infractionType?.let { InfractionTypeEnum.valueOf(it) },
            observations = observations,
            target = target.map { it.toInfractionEnvTargetEntity() }?.firstOrNull(),
            natinfs = natinfs
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity): InfractionModel {
            return InfractionModel(
                id = infraction.id,
                missionId = infraction.missionId,
                actionId = infraction.actionId,
                controlType = infraction.controlType.toString(),
                infractionType = infraction.infractionType?.toString(),
                natinfs = infraction.natinfs.orEmpty(),
                observations = infraction.observations,
                target = infraction.target?.let {
                    listOf(InfractionEnvTargetModel.fromInfractionEnvTargetEntity(it))
                } ?: emptyList()  // Provide an empty list if target is null
            )
        }

    }
}
