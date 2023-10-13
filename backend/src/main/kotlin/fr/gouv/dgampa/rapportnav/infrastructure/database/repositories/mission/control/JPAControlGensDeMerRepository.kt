package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.control

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlGensDeMer
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlGensDeMerRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.control.ControlGensDeMerModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.control.IDBControlGensDeMerRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class JPAControlGensDeMerRepository(
    private val dbControlGensDeMerRepository: IDBControlGensDeMerRepository,
    private val mapper: ObjectMapper,
) : IControlGensDeMerRepository {

    override fun findAllByMissionId(missionId: Int): List<ControlGensDeMer> {
        // TODO call correct function filtering by mission id
        TODO("Not yet implemented")
//        return dbControlGensDeMerRepository.findAll().map { it.toControlGensDeMer() }

    }

    @Transactional
    override fun save(control: ControlGensDeMer): ControlGensDeMer {
        return try {
            val controlModel = ControlGensDeMerModel.fromControlGensDeMer(control, mapper)
            dbControlGensDeMerRepository.save(controlModel).toControlGensDeMer()
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating GensDeMer Control", e)
        }
    }

}
