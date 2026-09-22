package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.sati

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiEntity
import fr.gouv.dgampa.rapportnav.domain.entities.mission.sati.SatiStatusType
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.sati.ISatiRepository
import java.util.UUID

@UseCase
class PatchSatiStatus(
    private val repository: ISatiRepository,
) {
    fun execute(id: UUID, status: SatiStatusType): SatiEntity? {
        return repository.updateStatus(id, status)
    }
}
