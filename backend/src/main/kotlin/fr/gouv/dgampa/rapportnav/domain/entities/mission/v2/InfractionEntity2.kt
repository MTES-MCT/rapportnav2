package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2.InfractionModel2
import java.util.*

class InfractionEntity2(
    val id: UUID,
    var controlId: UUID? = null,
    var observations: String? = null,
    var natinfs: List<String> = listOf(),
    val infractionType: InfractionTypeEnum? = null
) {

    fun toInfractionModel(): InfractionModel2 {
        return InfractionModel2(
            id = id,
            natinfs = natinfs,
            observations = observations,
            infractionType = infractionType?.toString()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InfractionEntity
        return id == other.id
            && controlId == other.controlId
            && observations == other.observations
            && Objects.equals(natinfs, other.natinfs)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + natinfs.hashCode()
        result = 31 * result + (controlId?.hashCode() ?: 0)
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (infractionType?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromInfractionModel(model: InfractionModel2): InfractionEntity2 {
            return InfractionEntity2(
                id = model.id,
                natinfs = model.natinfs,
                controlId = model.control?.id,
                observations = model.observations,
                infractionType = model.infractionType?.let { InfractionTypeEnum.valueOf(it) }
            )
        }
    }
}
