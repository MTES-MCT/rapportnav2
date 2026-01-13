package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBMissionGeneralInfoRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAMissionGeneralInfoRepository(
    private val dbRepo: IDBMissionGeneralInfoRepository,
) : IMissionGeneralInfoRepository {

    override fun findAll(): List<MissionGeneralInfoModel> {
        return dbRepo.findAll()
    }
    override fun findByMissionId(missionId: Int): Optional<MissionGeneralInfoModel> {
        return dbRepo.findByMissionId(missionId)
    }

    override fun findAllByMissionId(missionId: Int): List<MissionGeneralInfoModel> {
        return dbRepo.findAllByMissionId(missionId)
    }

    override fun findByMissionIdUUID(missionIdUUID: UUID): Optional<MissionGeneralInfoModel> {
        return dbRepo.findByMissionIdUUID(missionIdUUID)
    }

    override fun findAllByMissionIdUUID(missionIdUUID: UUID): List<MissionGeneralInfoModel> {
        return dbRepo.findAllByMissionIdUUID(missionIdUUID)
    }

    override fun findById(id: Int): Optional<MissionGeneralInfoModel> {
        return dbRepo.findById(id)
    }

    override fun existsById(id: Int): Boolean {
        return dbRepo.existsById(id)
    }

    override fun deleteById(id: Int) {
        return dbRepo.deleteById(id)
    }

    @Transactional
    override fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel {
        return try {
            val generalInfoModel = info.toMissionGeneralInfoModel()
            dbRepo.save(generalInfoModel)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_SAVE_EXCEPTION,
                message = "Unable to save MissionGeneralInfo='${info.id}'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Unable to prepare data before saving MissionGeneralInfo='${info.id}'",
                originalException = e
            )
        }
    }

}
