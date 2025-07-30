package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.generalInfo

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo.IMissionGeneralInfoRepository
import org.springframework.dao.IncorrectResultSizeDataAccessException
import org.springframework.transaction.annotation.Transactional

@UseCase
class AddOrUpdateMissionGeneralInfo(
    private val infoRepo: IMissionGeneralInfoRepository
) {
    @Transactional
    fun execute(info: MissionGeneralInfoEntity): MissionGeneralInfoEntity {
        val missionId = info.missionId
            ?: throw IllegalArgumentException("Mission ID cannot be null")

        // Handle potential duplicates
        val existingInfo = try {
            infoRepo.findByMissionId(missionId)
        } catch (e: IncorrectResultSizeDataAccessException) {
            // If duplicates exist, clean them up
            cleanupDuplicates(missionId)
            infoRepo.findByMissionId(missionId)
        }

        val dataToSave = if (existingInfo.isPresent) {
            // Update existing record
            val existing = existingInfo.get()
            info.copy(id = existing.id) // Preserve the existing ID
        } else {
            // Create new record
            info
        }

        val savedModel = infoRepo.save(dataToSave)
        return MissionGeneralInfoEntity.fromMissionGeneralInfoModel(savedModel)
    }

    private fun cleanupDuplicates(missionId: Int) {
        val allDuplicates = infoRepo.findAllByMissionId(missionId)
        if (allDuplicates.size > 1) {
            // Keep the most recent one, delete the rest
            val toKeep = allDuplicates.minByOrNull { it.id!! }
            val toDelete = allDuplicates.filter { it.id != toKeep?.id }

            toDelete.forEach { duplicate ->
                infoRepo.deleteById(duplicate.id!!)
            }
        }
    }
}
