package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.crew

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.crew.MissionCrewEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.crew.IMissionCrewRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.crew.MissionCrewModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgent2Repository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBAgentRoleRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.crew.IDBMissionCrewRepository
import jakarta.transaction.Transactional
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class JPAMissionCrewRepository(
    private val dbMissionCrewRepository: IDBMissionCrewRepository,
    private val dbAgentRepository: IDBAgent2Repository,
    private val dbAgentRoleRepository: IDBAgentRoleRepository,
) : IMissionCrewRepository {
    override fun findByMissionId(missionId: Int): List<MissionCrewModel> {
        return dbMissionCrewRepository.findByMissionId(missionId)
    }

    override fun findByMissionIdUUID(missionIdUUID: UUID): List<MissionCrewModel> {
        return dbMissionCrewRepository.findByMissionIdUUID(missionIdUUID)
    }

    @Transactional
    override fun save(crew: MissionCrewEntity): MissionCrewModel {
        return try {
            val crewModel = crew.toMissionCrewModel()
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
                message = "Unable to save MissionCrew='${crew.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionCrew='${crew.id}'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun deleteById(id: Int): Boolean {
        val crew: Optional<MissionCrewModel> = dbMissionCrewRepository.findById(id)
        if (crew.isPresent) {
            dbMissionCrewRepository.deleteById(id)
            return true;
        }
        throw NoSuchElementException("AgentCrew with ID $id not found")
    }
}
