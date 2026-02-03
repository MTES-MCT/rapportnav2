package fr.gouv.dgampa.rapportnav.domain.entities.mission.v2

import fr.gouv.dgampa.rapportnav.domain.entities.mission.env.envActions.InfractionTypeEnum
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.v2.InfractionModel
import java.util.*

class InfractionEntity(
    val id: UUID,
    var observations: String? = null,
    var natinfs: List<String> = listOf(),
    val infractionType: InfractionTypeEnum? = null
) {

    fun toInfractionModel(): InfractionModel {
        return InfractionModel(
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
            && observations == other.observations
            && Objects.equals(natinfs, other.natinfs)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + natinfs.hashCode()
        result = 31 * result + (observations?.hashCode() ?: 0)
        result = 31 * result + (infractionType?.hashCode() ?: 0)
        return result
    }

    companion object {
        fun fromInfractionModel(model: InfractionModel): fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.InfractionEntity {
            return InfractionEntity(
                id = model.id,
                natinfs = model.natinfs,
                observations = model.observations,
                infractionType = model.infractionType?.let { InfractionTypeEnum.valueOf(it) }
            )
        }
    }
}
