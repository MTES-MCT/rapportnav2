package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionGeneralInfoRepository : JpaRepository<MissionGeneralInfoModel, UUID> {
    fun findByMissionId(missionId: Int): Optional<MissionGeneralInfoModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): Optional<MissionGeneralInfoModel>

    fun findById(id: Int): Optional<MissionGeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel


}
