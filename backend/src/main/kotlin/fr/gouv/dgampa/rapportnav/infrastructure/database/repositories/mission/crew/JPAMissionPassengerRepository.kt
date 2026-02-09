package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.PassengerEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionPassengerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.passenger.PassengerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBMissionPassengerRepository
import jakarta.transaction.Transactional
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JPAMissionPassengerRepository(
    private val repo: IDBMissionPassengerRepository,
) : IMissionPassengerRepository {
    override fun findByMissionId(missionId: Int): List<PassengerModel> {
        return repo.findByMissionId(missionId)
    }

    override fun findByMissionIdUUID(missionIdUUID: UUID): List<PassengerModel> {
        return repo.findByMissionIdUUID(missionIdUUID)
    }

    @Transactional
    override fun save(passenger: PassengerEntity): PassengerModel {
        return try {
            val model = passenger.toPassengerModel()
            repo.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionPassenger='${passenger.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionPassenger='${passenger.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: Int): Boolean {
        val crew: Optional<PassengerModel> = repo.findById(id)
        if (crew.isPresent) {
            repo.deleteById(id)
            return true;
        }
        throw NoSuchElementException("MissionPassenger with ID $id not found")
    }
}
