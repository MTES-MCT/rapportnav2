package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.target2.v2

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.target2.v2.TargetModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBTargetRepository : JpaRepository<TargetModel, UUID> {

    override fun findById(id: UUID): Optional<TargetModel>

    fun findByActionId(actionId: String): List<TargetModel>

    fun findByExternalId(externalId: String): TargetModel?

    fun deleteByActionId(actionId: String)
}
