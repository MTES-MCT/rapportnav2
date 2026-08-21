package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface IDBMissionRepository: JpaRepository<MissionModel, UUID> {

    fun save(entity: MissionModel): MissionModel

    fun findAllByOrderByStartDateTimeUtcDesc(pageable: Pageable): Page<MissionModel>

    fun findByIdOrderByStartDateTimeUtcDesc(id: UUID, pageable: Pageable): Page<MissionModel>

    fun findByExternalIdOrderByStartDateTimeUtcDesc(externalId: String, pageable: Pageable): Page<MissionModel>

    @Query("""
    SELECT m FROM MissionModel m
    WHERE m.startDateTimeUtc >= :startBeforeDateTime
    AND m.endDateTimeUtc <= :endBeforeDateTime
    ORDER BY m.startDateTimeUtc DESC
    """)
    fun findAllBetweenDates(
        @Param("startBeforeDateTime") startBeforeDateTime: Instant,
        @Param("endBeforeDateTime") endBeforeDateTime: Instant
    ): List<MissionModel?>

    /**
     * Nav-only missions for a given service, newest first. Env-mirror rows have a null serviceId, so scoping by
     * serviceId naturally excludes them. Date bounds are always provided (callers substitute wide defaults when
     * no date filter is active) to keep the JPQL free of nullable-parameter handling.
     */
    @Query("""
    SELECT m FROM MissionModel m
    WHERE m.serviceId = :serviceId
    AND m.isDeleted = false
    AND m.startDateTimeUtc >= :startedAfter
    AND m.startDateTimeUtc <= :startedBefore
    ORDER BY m.startDateTimeUtc DESC
    """)
    fun findNavMissionsForService(
        @Param("serviceId") serviceId: Int,
        @Param("startedAfter") startedAfter: Instant,
        @Param("startedBefore") startedBefore: Instant
    ): List<MissionModel>

    override fun findById(id: UUID): Optional<MissionModel>

    fun findByExternalId(externalId: String): Optional<MissionModel>
}
