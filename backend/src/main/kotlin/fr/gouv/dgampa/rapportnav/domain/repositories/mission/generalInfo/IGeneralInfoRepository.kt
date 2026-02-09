package fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.GeneralInfoModel
import java.util.*

interface IGeneralInfoRepository {
    fun findByMissionId(missionId: Int): Optional<GeneralInfoModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): Optional<GeneralInfoModel>

    fun findById(id: Int): Optional<GeneralInfoModel>

    fun findAll(): List<GeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: GeneralInfoModel): GeneralInfoModel

    fun findAllByMissionId(missionId: Int): List<GeneralInfoModel>

    fun findAllByMissionIdUUID(missionIdUUID: UUID): List<GeneralInfoModel>

    fun deleteById(id: Int)

}
