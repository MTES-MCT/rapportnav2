package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.infraction

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.infraction.InfractionEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.infraction.IInfractionRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.infraction.InfractionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlAdministrativeRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlNavigationRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlSecurityRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.infraction.IDBInfractionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


@Repository
class JPAInfractionRepository(
    private val dbRepo: IDBInfractionRepository,
    private val dbControlAdministrativeRepo: IDBControlAdministrativeRepository,
    private val dbControlSecurityRepo: IDBControlSecurityRepository,
    private val dbControlNavigationRepo: IDBControlNavigationRepository,
    private val dbControlGensDeMerRepo: IDBControlGensDeMerRepository,
) : IInfractionRepository {

    @Transactional
    override fun save(infraction: InfractionEntity): InfractionModel {
        return try {
            val infractionModel = InfractionModel.fromInfractionEntity(infraction)

            val control = when (infraction.controlType) {
                ControlType.ADMINISTRATIVE -> infraction.controlId?.let {
                    dbControlAdministrativeRepo.findById(it).orElse(null)
                }

                ControlType.SECURITY -> infraction.controlId?.let { dbControlSecurityRepo.findById(it).orElse(null) }
                ControlType.NAVIGATION -> infraction.controlId?.let {
                    dbControlNavigationRepo.findById(it).orElse(null)
                }

                ControlType.GENS_DE_MER -> infraction.controlId?.let {
                    dbControlGensDeMerRepo.findById(it).orElse(null)
                }

                null -> null
            } ?: throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_FIND_CONTROL_FOR_INFRACTION_EXCEPTION,
                message = "Unable to find associated Control for Infraction='${infraction.id}'",
                data = infraction
            )

            infractionModel.control = control
            infractionModel.target?.map { it.infraction = infractionModel }
            dbRepo.save(infractionModel)

        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save Infraction='${infraction.id}'",
                e,
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        return dbRepo.deleteById(id)
    }


    override fun findAllByActionId(actionId: String): List<InfractionModel> {
        return dbRepo.findAllByActionId(actionId = actionId)
    }

    override fun findById(id: UUID): Optional<InfractionModel> {
        return dbRepo.findById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbRepo.existsById(id)
    }

}
