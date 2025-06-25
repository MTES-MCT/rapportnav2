package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.IMissionNavRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.IDBMissionRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
class JPAMissionNavRepository(
    private val dbRepository: IDBMissionRepository
) : IMissionNavRepository {
    override fun save(model: MissionModel): MissionModel {
        return try {
            dbRepository.save(model)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionNav='${model.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionNav='${model.id}'",
                originalException = e
            )
        }
    }

    override fun finById(id: UUID): Optional<MissionModel> {
        return dbRepository.findById(id)
    }

    override fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<MissionModel?> {
        return dbRepository.findAllBetweenDates(
            startBeforeDateTime = startBeforeDateTime,
            endBeforeDateTime = endBeforeDateTime
        )
    }
}
