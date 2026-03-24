package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.sati

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.sati.SatiModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface IDBSatiRepository : JpaRepository<SatiModel, UUID> {
    fun findByOwnerId(ownerId: UUID): SatiModel
}
