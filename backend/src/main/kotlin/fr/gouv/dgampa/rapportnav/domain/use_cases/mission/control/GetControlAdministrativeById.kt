package fr.gouv.dgampa.rapportnav.domain.use_cases.mission.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrativeEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository
import java.util.*

@UseCase
class GetControlAdministrativeById(private val repository: IControlAdministrativeRepository) {
    fun execute(id: UUID): ControlAdministrativeEntity? {
        if (this.repository.existsById(id = id)) {
            val controlModel = this.repository.findById(id = id).get()
            return controlModel.toControlAdministrativeEntity()
        }
        return null
    }
}
