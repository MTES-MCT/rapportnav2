package fr.gouv.dgampa.rapportnav.domain.repositories.mission

import fr.gouv.dgampa.rapportnav.domain.entities.mission.v2.MissionNavEntity
import fr.gouv.dgampa.rapportnav.infrastructure.database.model.mission.MissionModel
import java.time.Instant
import java.util.*

interface IMissionNavRepository {
    fun save(entity: MissionNavEntity): MissionModel

    fun finById(id: UUID): Optional<MissionModel>

    fun findAll(startBeforeDateTime: Instant, endBeforeDateTime: Instant): List<MissionModel?>
}
