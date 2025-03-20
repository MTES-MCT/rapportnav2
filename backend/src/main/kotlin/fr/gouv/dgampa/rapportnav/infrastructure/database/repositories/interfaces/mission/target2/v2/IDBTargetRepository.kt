package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel2
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBTargetRepository : JpaRepository<TargetModel2, UUID> {

    override fun findById(id: UUID): Optional<TargetModel2>

    fun findByActionId(actionId: UUID): List<TargetModel2>

    fun deleteByActionId(actionId: UUID)
}
