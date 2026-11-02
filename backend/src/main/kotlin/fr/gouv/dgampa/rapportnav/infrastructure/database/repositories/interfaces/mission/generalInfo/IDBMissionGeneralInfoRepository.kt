package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission.generalInfo

import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.generalInfo.MissionGeneralInfoEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.generalInfo.MissionGeneralInfoModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface IDBMissionGeneralInfoRepository : JpaRepository<MissionGeneralInfoModel, UUID> {
    fun findByMissionId(missionId: Int): Optional<MissionGeneralInfoModel>

    fun findByMissionIdUUID(missionIdUUID: UUID): Optional<MissionGeneralInfoModel>

    fun findById(id: Int): Optional<MissionGeneralInfoModel>

    fun existsById(id: Int): Boolean

    fun save(info: MissionGeneralInfoEntity): MissionGeneralInfoModel

    fun findAllByMissionId(missionId: Int): List<MissionGeneralInfoModel>

    fun findAllByMissionIdUUID(missionIdUUID: UUID): List<MissionGeneralInfoModel>

    override fun findAll(): List<MissionGeneralInfoModel>

    fun findAllByOrderByMissionIdDesc(pageable: Pageable): Page<MissionGeneralInfoModel>

    fun findByMissionIdOrderByMissionIdDesc(missionId: Int, pageable: Pageable): Page<MissionGeneralInfoModel>

    fun findByMissionIdUUIDOrderByMissionIdDesc(missionIdUUID: UUID, pageable: Pageable): Page<MissionGeneralInfoModel>

    fun deleteById(id: Int)

    // Left join so rows without a service are returned too (reported by the PAM backfill, not silently dropped).
    // service_type is a Postgres enum ("ServiceType") and the parameter is bound as varchar: compare as text, since
    // Postgres has no "ServiceType" = varchar operator.
    @Query(
        """
        SELECT g FROM MissionGeneralInfoModel g LEFT JOIN g.service s
        WHERE (s IS NULL OR cast(s.serviceType as String) = :serviceType)
          AND (g.distanceInNauticalMiles IS NOT NULL OR g.consumedGOInLiters IS NOT NULL
               OR g.consumedFuelInLiters IS NOT NULL)
        ORDER BY g.id
        """
    )
    fun findWithPamMetrics(
        @Param("serviceType") serviceType: String,
        pageable: Pageable
    ): Page<MissionGeneralInfoModel>
}
