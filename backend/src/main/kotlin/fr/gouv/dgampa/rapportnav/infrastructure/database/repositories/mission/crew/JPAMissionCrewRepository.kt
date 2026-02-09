package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.CrewEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.CrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBMissionCrewRepository
import jakarta.transaction.Transactional
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JPAMissionCrewRepository(
    private val dbMissionCrewRepository: IDBMissionCrewRepository,
    private val dbAgentRepository: IDBAgentRepository,
    private val dbAgentRoleRepository: IDBAgentRoleRepository,
) : IMissionCrewRepository {

    override fun findByMissionId(missionId: Int): List<CrewModel> {
        return try {
            dbMissionCrewRepository.findByMissionId(missionId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAMissionCrewRepository.findByMissionId failed for missionId=$missionId",
                originalException = e
            )
        }
    }

    override fun findByMissionIdUUID(missionIdUUID: UUID): List<CrewModel> {
        return try {
            dbMissionCrewRepository.findByMissionIdUUID(missionIdUUID)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAMissionCrewRepository.findByMissionIdUUID failed for missionIdUUID=$missionIdUUID",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(crew: CrewEntity): CrewModel {
        return try {
            val crewModel = crew.toCrewModel()
            val agent = dbAgentRepository.findById(crew.agent.id!!).orElseThrow()
            crewModel.agent = agent

            if (crew.role !== null) {
                val role = dbAgentRoleRepository.findById(crew.role.id!!).orElseThrow()
                crewModel.role = role
            } //TODO("A refactoriser pour ULAM (mettre au niveau d'un usecase ?)")

            dbMissionCrewRepository.save(crewModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "JPAMissionCrewRepository.save failed for id=${crew.id}",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAMissionCrewRepository.save failed for id=${crew.id}",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: Int): Boolean {
        try {
            val crew = dbMissionCrewRepository.findById(id)
            if (!crew.isPresent) {
                throw BackendUsageException(
                    code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                    message = "JPAMissionCrewRepository.deleteById: crew not found for id=$id"
                )
            }
            dbMissionCrewRepository.deleteById(id)
            return true
        } catch (e: BackendUsageException) {
            throw e
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "JPAMissionCrewRepository.deleteById failed for id=$id",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "JPAMissionCrewRepository.deleteById failed for id=$id",
                originalException = e
            )
        }
    }
}
