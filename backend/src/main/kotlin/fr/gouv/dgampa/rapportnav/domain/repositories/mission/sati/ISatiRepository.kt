package fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import java.util.UUID

interface ISatiRepository {
    fun findById(id: UUID): SatiEntity?
    fun findByActionId(actionId: String): SatiEntity?
    fun findAll(): List<SatiEntity>
    fun save(sati: SatiEntity): SatiEntity
    fun deleteById(id: UUID)
    fun existsById(id: UUID): Boolean
}
