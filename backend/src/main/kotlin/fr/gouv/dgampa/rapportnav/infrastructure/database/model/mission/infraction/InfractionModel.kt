package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "infraction")
@JsonIgnoreProperties("controlAdministrative")
data class InfractionModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

//    @Column(name = "control_id", nullable = false)
//    var controlId: UUID,

    @Column(name = "control_type", nullable = false)
    var controlType: String,

    @Column(name = "formal_notice", nullable = true)
    var formalNotice: Boolean? = null,

    @Column(name = "observations", nullable = true)
    var observations: String? = null,

    @Column(name = "deleted_at")
    var deletedAt: ZonedDateTime? = null,

    @OneToMany(mappedBy = "infraction", targetEntity = InfractionNatinfModel::class)
    var infractions: List<InfractionNatinfModel>? = null,

    @ManyToOne
    @JoinColumn(name = "control_id", referencedColumnName = "id")
    @JsonIgnore
    var controlAdministrative: ControlAdministrativeModel? = null

) {
    fun toInfractionEntity(): InfractionEntity {
        return InfractionEntity(
            id = id,
            missionId = missionId,
//            controlAdministrative = controlAdministrative?.toControlAdministrativeEntity(),
            controlType = ControlType.valueOf(controlType),
            formalNotice = formalNotice,
            observations = observations,
            deletedAt = deletedAt
        )
    }

    companion object {
        fun fromInfractionEntity(infraction: InfractionEntity) = InfractionModel(
            id = infraction.id,
            missionId = infraction.missionId,
//            controlAdministrative = ControlAdministrativeModel.fromControlAdministrativeEntity(infraction.controlAdministrative!!),
            controlType = infraction.controlType.toString(),
            formalNotice = infraction.formalNotice,
            observations = infraction.observations,
            deletedAt = infraction.deletedAt,
        )
    }
}
