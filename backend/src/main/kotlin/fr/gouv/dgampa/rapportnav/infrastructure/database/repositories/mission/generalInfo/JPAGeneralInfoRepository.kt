package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendInternalException
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageErrorCode
import fr.gouv.dgampa.rapportnav.domain.exceptions.BackendUsageException
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IGeneralInfoRepository
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo.IDBMissionGeneralInfoRepository
import org.springframework.dao.InvalidDataAccessApiUsageException
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
class JPAGeneralInfoRepository(
    private val dbRepo: IDBMissionGeneralInfoRepository,
) : IGeneralInfoRepository {

    override fun findAll(): List<GeneralInfoModel> {
        return try {
            dbRepo.findAll()
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find all MissionGeneralInfo",
                originalException = e
            )
        }
    }

    override fun findByMissionId(missionId: Int): Optional<GeneralInfoModel> {
        return try {
            dbRepo.findByMissionId(missionId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionGeneralInfo for missionId='$missionId'",
                originalException = e
            )
        }
    }

    override fun findAllByMissionId(missionId: Int): List<GeneralInfoModel> {
        return try {
            dbRepo.findAllByMissionId(missionId)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find all MissionGeneralInfo for missionId='$missionId'",
                originalException = e
            )
        }
    }

    override fun findByMissionIdUUID(missionIdUUID: UUID): Optional<GeneralInfoModel> {
        return try {
            dbRepo.findByMissionIdUUID(missionIdUUID)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionGeneralInfo for missionIdUUID='$missionIdUUID'",
                originalException = e
            )
        }
    }

    override fun findAllByMissionIdUUID(missionIdUUID: UUID): List<GeneralInfoModel> {
        return try {
            dbRepo.findAllByMissionIdUUID(missionIdUUID)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find all MissionGeneralInfo for missionIdUUID='$missionIdUUID'",
                originalException = e
            )
        }
    }

    override fun findById(id: Int): Optional<GeneralInfoModel> {
        return try {
            dbRepo.findById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to find MissionGeneralInfo with id='$id'",
                originalException = e
            )
        }
    }

    override fun existsById(id: Int): Boolean {
        return try {
            dbRepo.existsById(id)
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to check existence of MissionGeneralInfo with id='$id'",
                originalException = e
            )
        }
    }

    override fun deleteById(id: Int) {
        try {
            dbRepo.deleteById(id)
        } catch (e: InvalidDataAccessApiUsageException) {
            throw BackendUsageException(
                code = BackendUsageErrorCode.COULD_NOT_DELETE_EXCEPTION,
                message = "Unable to delete MissionGeneralInfo='$id'",
                e,
            )
        } catch (e: Exception) {
            throw BackendInternalException(
                message = "Failed to delete MissionGeneralInfo with id='$id'",
                originalException = e
            )
        }
    }

    @Transactional
    override fun save(info: GeneralInfoModel): GeneralInfoModel {
        return try {
            dbRepo.save(info)
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
