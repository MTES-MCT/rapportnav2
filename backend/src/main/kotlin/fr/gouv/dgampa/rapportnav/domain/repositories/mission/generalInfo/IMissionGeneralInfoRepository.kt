package fr.gouv.dgampa.rapportnav.domain.repositories.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page
import java.util.*

interface IMissionGeneralInfoRepository {
    fun findByMissionId(missionId: Int): Optional<MissionGeneralInfoModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): Optional<MissionGeneralInfoModel>

    fun findById(id: Int): Optional<MissionGeneralInfoModel>

    fun findAll(): List<MissionGeneralInfoModel>

    fun findAllPaginated(page: Int, size: Int): Page<MissionGeneralInfoModel>

    fun findByMissionIdPaginated(missionId: Int, page: Int, size: Int): Page<MissionGeneralInfoModel>

    fun findByMissionIdUUIDPaginated(missionIdUUID: UUID, page: Int, size: Int): Page<MissionGeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel

    fun findAllByMissionId(missionId: Int): List<MissionGeneralInfoModel>

    fun findAllByMissionIdUUID(missionIdUUID: UUID): List<MissionGeneralInfoModel>

    fun deleteById(id: Int)

}
