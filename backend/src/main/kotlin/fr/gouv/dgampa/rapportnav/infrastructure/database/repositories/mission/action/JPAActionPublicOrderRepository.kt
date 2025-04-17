package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.action

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.action.ActionPublicOrderEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.action.INavActionPublicOrderRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.action.ActionPublicOrderModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.action.IDBActionPublicOrderRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAActionPublicOrderRepository(
    private val dbActionPublicOrderRepository: IDBActionPublicOrderRepository,
) : INavActionPublicOrderRepository {
    override fun findAllByMissionId(missionId: String): List<ActionPublicOrderModel> {
        return dbActionPublicOrderRepository.findAllByMissionId(missionId)
    }

    override fun findById(id: UUID): Optional<ActionPublicOrderModel> {
        return dbActionPublicOrderRepository.findById(id)
    }

    @Transactional
    override fun save(publicOrderEntity: ActionPublicOrderEntity): ActionPublicOrderModel {
        return try {
            val publicOrderModel = ActionPublicOrderModel.fromPublicOrderEntity(publicOrderEntity)
            dbActionPublicOrderRepository.save(publicOrderModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save ActionPublicOrder='${publicOrderEntity.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving ActionPublicOrder='${publicOrderEntity.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: UUID) {
        dbActionPublicOrderRepository.deleteById(id)
    }

    override fun existsById(id: UUID): Boolean {
        return dbActionPublicOrderRepository.existsById(id)
    }
}
