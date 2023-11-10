package fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import java.util.*

interface IInfractionRepository {
    fun save(infraction: InfractionEntity): InfractionModel

    fun findAllByActionId(actionId: String): List<InfractionModel>

    fun deleteById(id: UUID)

    fun existsById(id: UUID): Boolean

    fun findById(id: UUID): Optional<InfractionModel>
}
