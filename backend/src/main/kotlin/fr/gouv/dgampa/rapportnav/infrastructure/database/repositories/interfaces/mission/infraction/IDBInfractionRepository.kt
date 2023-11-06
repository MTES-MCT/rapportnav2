package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBInfractionRepository: JpaRepository<InfractionModel, UUID> {
    fun save(infraction: InfractionEntity): InfractionModel

    override fun deleteById(id: UUID)
}
