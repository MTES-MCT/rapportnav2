package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlAdministrativeModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.infraction.IDBInfractionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAInfractionRepository(
    private val dbRepo: IDBInfractionRepository,
) : IInfractionRepository {

    @Transactional
    override fun save(infraction: InfractionEntity): InfractionModel {
        return try {
            val infractionModel = InfractionModel.fromInfractionEntity(infraction)
            infractionModel.controlAdministrative = ControlAdministrativeModel.fromControlAdministrativeEntity(infraction.controlAdministrative!!)
            dbRepo.save(infractionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating Infraction", e)
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbRepo.deleteById(id)
    }

}
