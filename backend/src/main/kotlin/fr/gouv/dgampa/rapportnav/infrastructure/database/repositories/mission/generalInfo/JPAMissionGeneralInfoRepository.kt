package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBMissionGeneralInfoRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAMissionGeneralInfoRepository (
    private val dbRepo: IDBMissionGeneralInfoRepository,
) : IMissionGeneralInfoRepository {

    override fun findByMissionId(missionId: Int): Optional<MissionGeneralInfoModel> {
        return dbRepo.findByMissionId(missionId)
    }

    override fun findById(id: Int): Optional<MissionGeneralInfoModel> {
        return dbRepo.findById(id)
    }

    override fun existsById(id: Int): Boolean {
        return dbRepo.existsById(id)
    }

    @Transactional
    override fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel {
        return try {
            val statusActionModel = MissionGeneralInfoModel.fromMissionGeneralInfoEntity(info)
            dbRepo.save(statusActionModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw Exception("Error saving or updating action status", e)
        }
    }

}
