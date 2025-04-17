package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselSizeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.VesselTypeEnum
import java.util.*

data class InfractionEnvTargetEntity(
    var id: UUID,
    var missionId: String,
    var actionId: String,
    var infractionId: UUID,
    val identityControlledPerson: String,
    var vesselType: VesselTypeEnum? = null,
    var vesselSize: VesselSizeEnum? = null,
    val vesselIdentifier: String? =  null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InfractionEnvTargetEntity
        return id == other.id
            && missionId == other.missionId
            && actionId == other.actionId
            && infractionId == other.infractionId
            && identityControlledPerson == other.identityControlledPerson
            && vesselType == other.vesselType
            && vesselSize == other.vesselSize
            && vesselIdentifier == other.vesselIdentifier
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + missionId.hashCode()
        result = 31 * result + actionId.hashCode()
        result = 31 * result + infractionId.hashCode()
        result = 31 * result + identityControlledPerson.hashCode()
        result = 31 * result + (vesselType?.hashCode() ?: 0)
        result = 31 * result + (vesselSize?.hashCode() ?: 0)
        result = 31 * result + (vesselIdentifier?.hashCode() ?: 0)
        return result
    }
}
