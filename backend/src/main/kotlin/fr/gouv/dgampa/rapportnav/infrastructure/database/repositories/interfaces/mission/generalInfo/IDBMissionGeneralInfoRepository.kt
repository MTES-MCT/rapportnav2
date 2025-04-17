package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionGeneralInfoRepository : JpaRepository<MissionGeneralInfoModel, Int> {
    fun findByMissionId(missionId: String): Optional<MissionGeneralInfoModel>

    fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel


}
