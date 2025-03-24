package fr.gouv.dgampa.rapportnav.domain.repositories.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import java.util.*

interface ITargetRepository {
    fun save(target: TargetModel2): TargetModel2

    fun findById(id: UUID): Optional<TargetModel2>

    fun findByActionId(actionId: String): List<TargetModel2>

    fun deleteById(id: UUID)

    fun deleteByActionId(actionId: String)
}
