package fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati

import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiModuleType
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

data class SatiListFilter(
    val module: SatiModuleType? = null,
    val status: SatiStatusType? = null,
    val actionId: String? = null,
    val search: String? = null
)

interface ISatiRepository {
    fun deleteById(id: UUID)
    fun findAll(): List<SatiEntity>
    fun existsById(id: UUID): Boolean
    fun findById(id: UUID): SatiEntity?
    fun save(sati: SatiEntity): SatiEntity
    fun findByActionId(actionId: String): SatiEntity?
    fun findAll(pageable: Pageable, filter: SatiListFilter): Page<SatiEntity>
    fun updateStatus(id: UUID, status: SatiStatusType): SatiEntity?
}
