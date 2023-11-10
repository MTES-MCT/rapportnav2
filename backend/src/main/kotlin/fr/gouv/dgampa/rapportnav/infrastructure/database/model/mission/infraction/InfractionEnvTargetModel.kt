package fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction

import com.fasterxml.jackson.annotation.JsonIgnore
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEnvTargetEntity
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

    @Column(name = "vessel_type", nullable = false)
    var vesselType: String,

    @Column(name = "vessel_size", nullable = false)
    var vesselSize: String,

    @Column(name = "vessel_identifier", nullable = false)
    val vesselIdentifier: String,

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
            vesselType = VesselTypeEnum.valueOf(vesselType),
            vesselSize = VesselSizeEnum.valueOf(vesselSize),
        )
    }

    companion object {
        fun fromInfractionEnvTargetEntity(infraction: InfractionEnvTargetEntity) = InfractionEnvTargetModel(
            id = infraction.id,
            missionId = infraction.missionId,
            actionId = infraction.actionId,
            vesselIdentifier = infraction.vesselIdentifier,
            identityControlledPerson = infraction.identityControlledPerson,
            vesselType = infraction.vesselType.toString(),
            vesselSize = infraction.vesselSize.toString(),
        )
    }
}
