package fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import java.util.*

interface ITargetRepository {
    fun save(target: TargetModel): TargetModel

    fun findById(id: UUID): Optional<TargetModel>

    fun findByActionId(actionId: String): List<TargetModel>

    fun findByExternalId(externalId: String): TargetModel?

    fun deleteById(id: UUID)

    fun deleteByActionId(actionId: String)
}
