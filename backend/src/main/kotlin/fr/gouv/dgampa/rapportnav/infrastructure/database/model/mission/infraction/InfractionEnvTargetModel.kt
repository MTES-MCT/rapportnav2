package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionAntiPollutionModel
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "infraction_env_target")
data class InfractionEnvTargetModel(
    @Id
    @Column(name = "id", unique = true, nullable = false)
    var id: UUID,

    @Column(name = "mission_id", nullable = false)
    var missionId: Int,

    @Column(name = "action_id", nullable = false)
    var actionId: String,

    @Column(name = "vessel_type", nullable = true)
    var vesselType: String? = null,

    @Column(name = "vessel_size", nullable = true)
    var vesselSize: String? = null,

    @Column(name = "vessel_identifier", nullable = true)
    val vesselIdentifier: String? = null,

    @Column(name = "identity_controlled_person", nullable = false)
    val identityControlledPerson: String,

    @ManyToOne
    @JoinColumn(name = "infraction_id", referencedColumnName = "id")
    @JsonIgnore
    var infraction: InfractionModel? = null,


    ) {
    fun toInfractionEnvTargetEntity(): InfractionEnvTargetEntity {
        return InfractionEnvTargetEntity(
            id = id,
            missionId = missionId,
            actionId = actionId,
            infractionId = infraction!!.id,
            vesselIdentifier = vesselIdentifier,
            identityControlledPerson = identityControlledPerson,
            vesselType = vesselType?.let { VesselTypeEnum.valueOf(it) },
            vesselSize = vesselSize?.let { VesselSizeEnum.valueOf(it) },
        )
    }

    companion object {
        fun fromInfractionEnvTargetEntity(
            infractionTarget: InfractionEnvTargetEntity,
            infraction: InfractionEntity? = null
        ) =
            InfractionEnvTargetModel(
                id = infractionTarget.id,
                missionId = infractionTarget.missionId,
                actionId = infractionTarget.actionId,
                infraction = infraction?.let { InfractionModel.fromInfractionEntity(it) },
                vesselIdentifier = infractionTarget.vesselIdentifier,
                identityControlledPerson = infractionTarget.identityControlledPerson,
                vesselType = infractionTarget.vesselType?.toString(),
                vesselSize = infractionTarget.vesselSize?.toString(),
            )
    }

    override fun hashCode(): Int {
        return Objects.hash(id)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as InfractionEnvTargetModel
        return id == other.id
    }
}
