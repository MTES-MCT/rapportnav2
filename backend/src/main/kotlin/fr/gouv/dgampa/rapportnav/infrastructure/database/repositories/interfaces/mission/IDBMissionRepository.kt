package fr.gouv.dgampa.rapportnav.infrastructure.database.repositories.interfaces.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant
import java.util.*

interface IDBMissionRepository: JpaRepository<MissionModel, UUID> {

    fun save(entity: MissionModel): MissionModel

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

    override fun findById(id: UUID): Optional<MissionModel>
}
