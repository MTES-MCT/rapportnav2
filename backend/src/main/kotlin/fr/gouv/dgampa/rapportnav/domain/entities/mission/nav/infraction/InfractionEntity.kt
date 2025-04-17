package fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import java.util.*

data class InfractionEntity(
    val id: UUID,
    var missionId: String,
    var actionId: String,
    var controlId: UUID? = null,
    var controlType: ControlType? = null,
    val infractionType: InfractionTypeEnum? = null,
    var observations: String? = null,
    var natinfs: List<String>? = null,
    var target: InfractionEnvTargetEntity? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InfractionEntity
        return id == other.id
            && missionId == other.missionId
            && actionId == other.actionId
            && controlId == other.controlId
            && controlType == other.controlType
            && observations == other.observations
            && Objects.equals(target, other.target)
            && Objects.equals(natinfs, other.natinfs)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + missionId.hashCode()
        result = 31 * result + actionId.hashCode()
        result = 31 * result + (controlId?.hashCode() ?: 0)
        result = 31 * result + (controlType?.hashCode() ?: 0)
        result = 31 * result + (infractionType?.hashCode() ?: 0)
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (natinfs?.hashCode() ?: 0)
        result = 31 * result + (target?.hashCode() ?: 0)
        return result
    }
}
