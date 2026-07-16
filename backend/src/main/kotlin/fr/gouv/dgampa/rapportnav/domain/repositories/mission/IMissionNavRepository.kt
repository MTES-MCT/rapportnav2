package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import org.springframework.data.domain.Page
import java.time.Instant
import java.util.*

interface IMissionNavRepository {
    fun save(model: MissionModel): MissionModel

    fun finById(id: UUID): Optional<MissionModel>

    fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<MissionModel?>

    fun findAllPaginated(page: Int, size: Int): Page<MissionModel>

    fun deleteById(id: UUID)

    fun softDeleteById(id: UUID)
}
