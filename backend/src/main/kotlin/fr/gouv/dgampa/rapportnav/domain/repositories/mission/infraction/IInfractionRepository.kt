package fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import jakarta.transaction.Transactional
import java.util.*

interface IInfractionRepository {
    @Transactional
    fun save(infraction: InfractionEntity): InfractionModel


    fun deleteById(id: UUID)
}
