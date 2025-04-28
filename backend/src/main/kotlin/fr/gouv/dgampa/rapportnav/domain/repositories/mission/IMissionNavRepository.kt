package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.*

interface IMissionNavRepository {
    fun save(model: MissionModel): MissionModel

    fun finById(id: String): Optional<MissionModel>

    fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<MissionModel?>
}
