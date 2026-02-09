package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionGeneralInfoRepository : JpaRepository<GeneralInfoModel, UUID> {
    fun findByMissionId(missionId: Int): Optional<GeneralInfoModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): Optional<GeneralInfoModel>

    fun findById(id: Int): Optional<GeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: GeneralInfoModel): GeneralInfoModel

    fun findAllByMissionId(missionId: Int): List<GeneralInfoModel>

    fun findAllByMissionIdUUID(missionIdUUID: UUID): List<GeneralInfoModel>

    override fun findAll(): List<GeneralInfoModel>

    fun deleteById(id: Int)
}
