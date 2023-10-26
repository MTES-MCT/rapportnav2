package fr.gouv.dgampa.rapportnav.domain.use_cases.missions.control

import fr.gouv.dgampa.rapportnav.config.UseCase
import fr.gouv.dgampa.rapportnav.domain.entities.mission.nav.control.ControlSecurityEntity
import fr.gouv.dgampa.rapportnav.domain.repositories.mission.control.IControlSecurityRepository

@UseCase
class AddOrUpdateControlSecurity(private val repository: IControlSecurityRepository) {
    fun execute(control: ControlSecurityEntity): ControlSecurityEntity {
        val savedData = this.repository.save(control).toControlSecurityEntity()
        return savedData
    }
}
