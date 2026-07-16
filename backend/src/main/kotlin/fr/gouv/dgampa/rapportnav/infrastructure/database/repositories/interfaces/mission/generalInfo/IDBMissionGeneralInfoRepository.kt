package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface IDBMissionGeneralInfoRepository : JpaRepository<MissionGeneralInfoModel, UUID> {
    fun findByMissionId(missionId: UUID): Optional<MissionGeneralInfoModel>

    fun findById(id: Int): Optional<MissionGeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel

    fun findAllByMissionId(missionId: UUID): List<MissionGeneralInfoModel>

    override fun findAll(): List<MissionGeneralInfoModel>

    fun findAllByOrderByMissionIdDesc(pageable: Pageable): Page<MissionGeneralInfoModel>

    fun findByMissionIdOrderByMissionIdDesc(missionId: UUID, pageable: Pageable): Page<MissionGeneralInfoModel>

    fun deleteById(id: Int)
}
