package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlAdministrative
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlAdministrativeRepository

@UseCase
class AddOrUpdateControlAdministrative(private val repository: IControlAdministrativeRepository) {
    fun execute(control: ControlAdministrative): ControlAdministrative {
        val savedData = this.repository.save(control).toControlAdministrative()
        return savedData
    }
}
